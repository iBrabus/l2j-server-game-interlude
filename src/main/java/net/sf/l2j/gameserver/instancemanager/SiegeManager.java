/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.instancemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

public class SiegeManager
{
	private static final Logger _log = Logger.getLogger(SiegeManager.class.getName());
	
	private static SiegeManager _instance;
	
	public static final SiegeManager getInstance()
	{
		if (_instance == null)
		{
			System.out.println("Initializing SiegeManager");
			_instance = new SiegeManager();
			_instance.load();
		}
		return _instance;
	}
	
	private int _attackerMaxClans = 500; // Max number of clans
	private int _attackerRespawnDelay = 20000; // Time in ms. Changeable in siege.config
	private int _defenderMaxClans = 500; // Max number of clans
	private int _defenderRespawnDelay = 10000; // Time in ms. Changeable in siege.config
	
	// Siege settings
	private FastMap<Integer, FastList<SiegeSpawn>> _artefactSpawnList;
	private FastMap<Integer, FastList<SiegeSpawn>> _controlTowerSpawnList;
	
	private int _controlTowerLosePenalty = 20000; // Time in ms. Changeable in siege.config
	private int _flagMaxCount = 1; // Changeable in siege.config
	private int _siegeClanMinLevel = 4; // Changeable in siege.config
	private int _siegeLength = 120; // Time in minute. Changeable in siege.config
	private List<Siege> _sieges;
	
	private SiegeManager()
	{
	}
	
	public final void addSiegeSkills(L2PcInstance character)
	{
		character.addSkill(SkillTable.getInstance().getInfo(246, 1), false);
		character.addSkill(SkillTable.getInstance().getInfo(247, 1), false);
	}
	
	/**
	 * Return true if character summon.
	 * @param activeChar The L2Character of the character can summon
	 * @param isCheckOnly
	 * @return
	 */
	public final boolean checkIfOkToSummon(L2Character activeChar, boolean isCheckOnly)
	{
		if ((activeChar == null) || !(activeChar instanceof L2PcInstance))
		{
			return false;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
		L2PcInstance player = (L2PcInstance) activeChar;
		Castle castle = CastleManager.getInstance().getCastle(player);
		
		if ((castle == null) || (castle.getCastleId() <= 0))
		{
			sm.addString("You must be on castle ground to summon this");
		}
		else if (!castle.getSiege().getIsInProgress())
		{
			sm.addString("You can only summon this during a siege.");
		}
		else if ((player.getClanId() != 0) && (castle.getSiege().getAttackerClan(player.getClanId()) == null))
		{
			sm.addString("You can only summon this as a registered attacker.");
		}
		else
		{
			return true;
		}
		
		if (!isCheckOnly)
		{
			player.sendPacket(sm);
		}
		return false;
	}
	
	/**
	 * Return true if the clan is registered or owner of a castle.
	 * @param clan The L2Clan of the player
	 * @param castleid
	 * @return
	 */
	public final boolean checkIsRegistered(L2Clan clan, int castleid)
	{
		if (clan == null)
		{
			return false;
		}
		
		if (clan.getHasCastle() > 0)
		{
			return true;
		}
		
		java.sql.Connection con = null;
		boolean register = false;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM siege_clans where clan_id=? and castle_id=?");
			statement.setInt(1, clan.getClanId());
			statement.setInt(2, castleid);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next())
			{
				register = true;
				break;
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception: checkIsRegistered(): " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
		return register;
	}
	
	public final void removeSiegeSkills(L2PcInstance character)
	{
		character.removeSkill(SkillTable.getInstance().getInfo(246, 1));
		character.removeSkill(SkillTable.getInstance().getInfo(247, 1));
	}
	
	private final void load()
	{
		try
		{
			InputStream is = new FileInputStream(new File(Config.SIEGE_CONFIGURATION_FILE));
			Properties siegeSettings = new Properties();
			siegeSettings.load(is);
			is.close();
			
			// Siege setting
			_attackerMaxClans = Integer.decode(siegeSettings.getProperty("AttackerMaxClans", "500"));
			_attackerRespawnDelay = Integer.decode(siegeSettings.getProperty("AttackerRespawn", "30000"));
			_controlTowerLosePenalty = Integer.decode(siegeSettings.getProperty("CTLossPenalty", "20000"));
			_defenderMaxClans = Integer.decode(siegeSettings.getProperty("DefenderMaxClans", "500"));
			_defenderRespawnDelay = Integer.decode(siegeSettings.getProperty("DefenderRespawn", "20000"));
			_flagMaxCount = Integer.decode(siegeSettings.getProperty("MaxFlags", "1"));
			_siegeClanMinLevel = Integer.decode(siegeSettings.getProperty("SiegeClanMinLevel", "4"));
			_siegeLength = Integer.decode(siegeSettings.getProperty("SiegeLength", "120"));
			
			// Siege spawns settings
			_controlTowerSpawnList = new FastMap<>();
			_artefactSpawnList = new FastMap<>();
			
			for (Castle castle : CastleManager.getInstance().getCastles())
			{
				FastList<SiegeSpawn> _controlTowersSpawns = new FastList<>();
				
				for (int i = 1; i < 0xFF; i++)
				{
					String _spawnParams = siegeSettings.getProperty(castle.getName() + "ControlTower" + Integer.toString(i), "");
					
					if (_spawnParams.length() == 0)
					{
						break;
					}
					
					StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
					
					try
					{
						int x = Integer.parseInt(st.nextToken());
						int y = Integer.parseInt(st.nextToken());
						int z = Integer.parseInt(st.nextToken());
						int npc_id = Integer.parseInt(st.nextToken());
						int hp = Integer.parseInt(st.nextToken());
						
						_controlTowersSpawns.add(new SiegeSpawn(castle.getCastleId(), x, y, z, 0, npc_id, hp));
					}
					catch (Exception e)
					{
						_log.warning("Error while loading control tower(s) for " + castle.getName() + " castle.");
					}
				}
				
				FastList<SiegeSpawn> _artefactSpawns = new FastList<>();
				
				for (int i = 1; i < 0xFF; i++)
				{
					String _spawnParams = siegeSettings.getProperty(castle.getName() + "Artefact" + Integer.toString(i), "");
					
					if (_spawnParams.length() == 0)
					{
						break;
					}
					
					StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
					
					try
					{
						int x = Integer.parseInt(st.nextToken());
						int y = Integer.parseInt(st.nextToken());
						int z = Integer.parseInt(st.nextToken());
						int heading = Integer.parseInt(st.nextToken());
						int npc_id = Integer.parseInt(st.nextToken());
						
						_artefactSpawns.add(new SiegeSpawn(castle.getCastleId(), x, y, z, heading, npc_id));
					}
					catch (Exception e)
					{
						_log.warning("Error while loading artefact(s) for " + castle.getName() + " castle.");
					}
				}
				
				_controlTowerSpawnList.put(castle.getCastleId(), _controlTowersSpawns);
				_artefactSpawnList.put(castle.getCastleId(), _artefactSpawns);
			}
			
		}
		catch (Exception e)
		{
			// _initialized = false;
			System.err.println("Error while loading siege data.");
			e.printStackTrace();
		}
	}
	
	public final FastList<SiegeSpawn> getArtefactSpawnList(int _castleId)
	{
		if (_artefactSpawnList.containsKey(_castleId))
		{
			return _artefactSpawnList.get(_castleId);
		}
		return null;
	}
	
	public final FastList<SiegeSpawn> getControlTowerSpawnList(int _castleId)
	{
		if (_controlTowerSpawnList.containsKey(_castleId))
		{
			return _controlTowerSpawnList.get(_castleId);
		}
		return null;
	}
	
	public final int getAttackerMaxClans()
	{
		return _attackerMaxClans;
	}
	
	public final int getAttackerRespawnDelay()
	{
		return _attackerRespawnDelay;
	}
	
	public final int getControlTowerLosePenalty()
	{
		return _controlTowerLosePenalty;
	}
	
	public final int getDefenderMaxClans()
	{
		return _defenderMaxClans;
	}
	
	public final int getDefenderRespawnDelay()
	{
		return (_defenderRespawnDelay);
	}
	
	public final int getFlagMaxCount()
	{
		return _flagMaxCount;
	}
	
	public final Siege getSiege(L2Object activeObject)
	{
		return getSiege(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public final Siege getSiege(int x, int y, int z)
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (castle.getSiege().checkIfInZone(x, y, z))
			{
				return castle.getSiege();
			}
		}
		return null;
	}
	
	public final int getSiegeClanMinLevel()
	{
		return _siegeClanMinLevel;
	}
	
	public final int getSiegeLength()
	{
		return _siegeLength;
	}
	
	public final List<Siege> getSieges()
	{
		if (_sieges == null)
		{
			_sieges = new FastList<>();
		}
		return _sieges;
	}
	
	public class SiegeSpawn
	{
		Location _location;
		private final int _npcId;
		private final int _heading;
		private final int _castleId;
		private int _hp;
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id)
		{
			_castleId = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npcId = npc_id;
		}
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id, int hp)
		{
			_castleId = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npcId = npc_id;
			_hp = hp;
		}
		
		public int getCastleId()
		{
			return _castleId;
		}
		
		public int getNpcId()
		{
			return _npcId;
		}
		
		public int getHeading()
		{
			return _heading;
		}
		
		public int getHp()
		{
			return _hp;
		}
		
		public Location getLocation()
		{
			return _location;
		}
	}
}
