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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * @version $Revision: 1.1.2.2.2.1 $ $Date: 2005/03/02 15:38:36 $
 */
public class ManaHeal implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.MANAHEAL,
		SkillType.MANARECHARGE,
		SkillType.MANAHEAL_PERCENT
	};
	
	@Override
	public void useSkill(L2Character actChar, L2Skill skill, L2Object[] targets)
	{
		L2Character target = null;
		
		for (L2Object target2 : targets)
		{
			target = (L2Character) target2;
			double mp = skill.getPower();
			if (skill.getSkillType() == SkillType.MANAHEAL_PERCENT)
			{
				// double mp = skill.getPower();
				mp = (target.getMaxMp() * mp) / 100.0;
			}
			else
			{
				mp = (skill.getSkillType() == SkillType.MANARECHARGE) ? target.calcStat(Stats.RECHARGE_MP_RATE, mp, null, null) : mp;
			}
			// int cLev = activeChar.getLevel();
			// hp += skill.getPower()/*+(Math.sqrt(cLev)*cLev)+cLev*/;
			target.setLastHealAmount((int) mp);
			target.setCurrentMp(mp + target.getCurrentMp());
			StatusUpdate sump = new StatusUpdate(target.getObjectId());
			sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
			target.sendPacket(sump);
			
			if ((actChar instanceof L2PcInstance) && (actChar != target))
			{
				SystemMessage sm = new SystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1);
				sm.addString(actChar.getName());
				sm.addNumber((int) mp);
				target.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessageId.S1_MP_RESTORED);
				sm.addNumber((int) mp);
				target.sendPacket(sm);
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}
