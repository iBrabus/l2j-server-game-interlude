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
package net.sf.l2j.gameserver.model.base;

import static net.sf.l2j.gameserver.model.base.ClassLevel.First;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Fourth;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Second;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Third;
import static net.sf.l2j.gameserver.model.base.ClassType.Fighter;
import static net.sf.l2j.gameserver.model.base.ClassType.Mystic;
import static net.sf.l2j.gameserver.model.base.ClassType.Priest;
import static net.sf.l2j.gameserver.model.base.PlayerRace.DarkElf;
import static net.sf.l2j.gameserver.model.base.PlayerRace.Dwarf;
import static net.sf.l2j.gameserver.model.base.PlayerRace.Human;
import static net.sf.l2j.gameserver.model.base.PlayerRace.LightElf;
import static net.sf.l2j.gameserver.model.base.PlayerRace.Orc;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */
public enum PlayerClass
{
	HumanFighter(Human, Fighter, First),
	Warrior(Human, Fighter, Second),
	Gladiator(Human, Fighter, Third),
	Warlord(Human, Fighter, Third),
	HumanKnight(Human, Fighter, Second),
	Paladin(Human, Fighter, Third),
	DarkAvenger(Human, Fighter, Third),
	Rogue(Human, Fighter, Second),
	TreasureHunter(Human, Fighter, Third),
	Hawkeye(Human, Fighter, Third),
	HumanMystic(Human, Mystic, First),
	HumanWizard(Human, Mystic, Second),
	Sorceror(Human, Mystic, Third),
	Necromancer(Human, Mystic, Third),
	Warlock(Human, Mystic, Third),
	Cleric(Human, Priest, Second),
	Bishop(Human, Priest, Third),
	Prophet(Human, Priest, Third),
	
	ElvenFighter(LightElf, Fighter, First),
	ElvenKnight(LightElf, Fighter, Second),
	TempleKnight(LightElf, Fighter, Third),
	Swordsinger(LightElf, Fighter, Third),
	ElvenScout(LightElf, Fighter, Second),
	Plainswalker(LightElf, Fighter, Third),
	SilverRanger(LightElf, Fighter, Third),
	ElvenMystic(LightElf, Mystic, First),
	ElvenWizard(LightElf, Mystic, Second),
	Spellsinger(LightElf, Mystic, Third),
	ElementalSummoner(LightElf, Mystic, Third),
	ElvenOracle(LightElf, Priest, Second),
	ElvenElder(LightElf, Priest, Third),
	
	DarkElvenFighter(DarkElf, Fighter, First),
	PalusKnight(DarkElf, Fighter, Second),
	ShillienKnight(DarkElf, Fighter, Third),
	Bladedancer(DarkElf, Fighter, Third),
	Assassin(DarkElf, Fighter, Second),
	AbyssWalker(DarkElf, Fighter, Third),
	PhantomRanger(DarkElf, Fighter, Third),
	DarkElvenMystic(DarkElf, Mystic, First),
	DarkElvenWizard(DarkElf, Mystic, Second),
	Spellhowler(DarkElf, Mystic, Third),
	PhantomSummoner(DarkElf, Mystic, Third),
	ShillienOracle(DarkElf, Priest, Second),
	ShillienElder(DarkElf, Priest, Third),
	
	OrcFighter(Orc, Fighter, First),
	OrcRaider(Orc, Fighter, Second),
	Destroyer(Orc, Fighter, Third),
	OrcMonk(Orc, Fighter, Second),
	Tyrant(Orc, Fighter, Third),
	OrcMystic(Orc, Mystic, First),
	OrcShaman(Orc, Mystic, Second),
	Overlord(Orc, Mystic, Third),
	Warcryer(Orc, Mystic, Third),
	
	DwarvenFighter(Dwarf, Fighter, First),
	DwarvenScavenger(Dwarf, Fighter, Second),
	BountyHunter(Dwarf, Fighter, Third),
	DwarvenArtisan(Dwarf, Fighter, Second),
	Warsmith(Dwarf, Fighter, Third),
	
	dummyEntry1(null, null, null),
	dummyEntry2(null, null, null),
	dummyEntry3(null, null, null),
	dummyEntry4(null, null, null),
	dummyEntry5(null, null, null),
	dummyEntry6(null, null, null),
	dummyEntry7(null, null, null),
	dummyEntry8(null, null, null),
	dummyEntry9(null, null, null),
	dummyEntry10(null, null, null),
	dummyEntry11(null, null, null),
	dummyEntry12(null, null, null),
	dummyEntry13(null, null, null),
	dummyEntry14(null, null, null),
	dummyEntry15(null, null, null),
	dummyEntry16(null, null, null),
	dummyEntry17(null, null, null),
	dummyEntry18(null, null, null),
	dummyEntry19(null, null, null),
	dummyEntry20(null, null, null),
	dummyEntry21(null, null, null),
	dummyEntry22(null, null, null),
	dummyEntry23(null, null, null),
	dummyEntry24(null, null, null),
	dummyEntry25(null, null, null),
	dummyEntry26(null, null, null),
	dummyEntry27(null, null, null),
	dummyEntry28(null, null, null),
	dummyEntry29(null, null, null),
	dummyEntry30(null, null, null),
	
	/*
	 * (3rd classes)
	 */
	duelist(Human, Fighter, Fourth),
	dreadnought(Human, Fighter, Fourth),
	phoenixKnight(Human, Fighter, Fourth),
	hellKnight(Human, Fighter, Fourth),
	sagittarius(Human, Fighter, Fourth),
	adventurer(Human, Fighter, Fourth),
	archmage(Human, Mystic, Fourth),
	soultaker(Human, Mystic, Fourth),
	arcanaLord(Human, Mystic, Fourth),
	cardinal(Human, Mystic, Fourth),
	hierophant(Human, Mystic, Fourth),
	
	evaTemplar(LightElf, Fighter, Fourth),
	swordMuse(LightElf, Fighter, Fourth),
	windRider(LightElf, Fighter, Fourth),
	moonlightSentinel(LightElf, Fighter, Fourth),
	mysticMuse(LightElf, Mystic, Fourth),
	elementalMaster(LightElf, Mystic, Fourth),
	evaSaint(LightElf, Mystic, Fourth),
	
	shillienTemplar(DarkElf, Fighter, Fourth),
	spectralDancer(DarkElf, Fighter, Fourth),
	ghostHunter(DarkElf, Fighter, Fourth),
	ghostSentinel(DarkElf, Fighter, Fourth),
	stormScreamer(DarkElf, Mystic, Fourth),
	spectralMaster(DarkElf, Mystic, Fourth),
	shillienSaint(DarkElf, Mystic, Fourth),
	
	titan(Orc, Fighter, Fourth),
	grandKhauatari(Orc, Fighter, Fourth),
	dominator(Orc, Mystic, Fourth),
	doomcryer(Orc, Mystic, Fourth),
	
	fortuneSeeker(Dwarf, Fighter, Fourth),
	maestro(Dwarf, Fighter, Fourth);
	
	private PlayerRace _race;
	private ClassLevel _level;
	private ClassType _type;
	
	private static final Set<PlayerClass> mainSubclassSet;
	private static final Set<PlayerClass> neverSubclassed = EnumSet.of(Overlord, Warsmith);
	
	private static final Set<PlayerClass> subclasseSet1 = EnumSet.of(DarkAvenger, Paladin, TempleKnight, ShillienKnight);
	private static final Set<PlayerClass> subclasseSet2 = EnumSet.of(TreasureHunter, AbyssWalker, Plainswalker);
	private static final Set<PlayerClass> subclasseSet3 = EnumSet.of(Hawkeye, SilverRanger, PhantomRanger);
	private static final Set<PlayerClass> subclasseSet4 = EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner);
	private static final Set<PlayerClass> subclasseSet5 = EnumSet.of(Sorceror, Spellsinger, Spellhowler);
	
	private static final EnumMap<PlayerClass, Set<PlayerClass>> subclassSetMap = new EnumMap<>(PlayerClass.class);
	
	static
	{
		Set<PlayerClass> subclasses = getSet(null, Third);
		subclasses.removeAll(neverSubclassed);
		
		mainSubclassSet = subclasses;
		
		subclassSetMap.put(DarkAvenger, subclasseSet1);
		subclassSetMap.put(Paladin, subclasseSet1);
		subclassSetMap.put(TempleKnight, subclasseSet1);
		subclassSetMap.put(ShillienKnight, subclasseSet1);
		
		subclassSetMap.put(TreasureHunter, subclasseSet2);
		subclassSetMap.put(AbyssWalker, subclasseSet2);
		subclassSetMap.put(Plainswalker, subclasseSet2);
		
		subclassSetMap.put(Hawkeye, subclasseSet3);
		subclassSetMap.put(SilverRanger, subclasseSet3);
		subclassSetMap.put(PhantomRanger, subclasseSet3);
		
		subclassSetMap.put(Warlock, subclasseSet4);
		subclassSetMap.put(ElementalSummoner, subclasseSet4);
		subclassSetMap.put(PhantomSummoner, subclasseSet4);
		
		subclassSetMap.put(Sorceror, subclasseSet5);
		subclassSetMap.put(Spellsinger, subclasseSet5);
		subclassSetMap.put(Spellhowler, subclasseSet5);
	}
	
	PlayerClass(PlayerRace pRace, ClassType pType, ClassLevel pLevel)
	{
		_race = pRace;
		_level = pLevel;
		_type = pType;
	}
	
	public final Set<PlayerClass> getAvailableSubclasses()
	{
		Set<PlayerClass> subclasses = null;
		
		if (_level == Third)
		{
			subclasses = EnumSet.copyOf(mainSubclassSet);
			
			subclasses.removeAll(neverSubclassed);
			subclasses.remove(this);
			
			switch (_race)
			{
				case LightElf:
					subclasses.removeAll(getSet(DarkElf, Third));
					break;
				case DarkElf:
					subclasses.removeAll(getSet(LightElf, Third));
					break;
			}
			
			Set<PlayerClass> unavailableClasses = subclassSetMap.get(this);
			
			if (unavailableClasses != null)
			{
				subclasses.removeAll(unavailableClasses);
			}
		}
		
		return subclasses;
	}
	
	public static final EnumSet<PlayerClass> getSet(PlayerRace race, ClassLevel level)
	{
		EnumSet<PlayerClass> allOf = EnumSet.noneOf(PlayerClass.class);
		
		for (PlayerClass playerClass : EnumSet.allOf(PlayerClass.class))
		{
			if ((race == null) || playerClass.isOfRace(race))
			{
				if ((level == null) || playerClass.isOfLevel(level))
				{
					allOf.add(playerClass);
				}
			}
		}
		
		return allOf;
	}
	
	public final boolean isOfRace(PlayerRace pRace)
	{
		return _race == pRace;
	}
	
	public final boolean isOfType(ClassType pType)
	{
		return _type == pType;
	}
	
	public final boolean isOfLevel(ClassLevel pLevel)
	{
		return _level == pLevel;
	}
	
	public final ClassLevel getLevel()
	{
		return _level;
	}
}
