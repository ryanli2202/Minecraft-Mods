"""Original Deep Night fiction. Dimension IDs verified against the installed pack; no mod prose copied."""
import json
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'src/main/resources/assets/deepnightguide'
chapters={}
dimensions={}
def chapter(key,title,icon,sections):chapters[key]={'title':title,'icon':'minecraft:'+icon,'sections':sections}
def dimension(key,title,icon,story,notes=None):
    dimensions[key]={'title':title,'icon':'minecraft:'+icon,'sections':{'Chronicle':story}}
    if notes:dimensions[key]['sections']['Field notes']=notes

chapter('01_the_deep_night','01 | The Deep Night','lantern',{
 'The letter in the ashes':[
  'The first thing you find is a letter wrapped in oilcloth. Its seal is a sun split down the middle. Most of the writing has washed away. One line survives: If anyone still walks the Ash Road, bring a light. We are not all dead.',
  'They call this age the Deep Night. Dawn still comes, wheat still grows, and villages still ring their bells. The name belongs to something harder to measure: roads whose keepers have vanished, graves that refuse to stay closed, and old powers collecting debts nobody remembers agreeing to pay.'],
 'The road before you':[
  'You arrive without a crown, a prophecy, or a place in the royal ledger. That may be why the surviving scribes trust you. You can choose what deserves protecting before somebody tells you what it is worth.',
  'Begin with a roof, a meal, and a route home. Beyond them lie the ruins of kingdoms, the territories of dragons, and doorways into worlds that have never heard of sunrise. Keep this book. Leave room in its margins for the things its authors got wrong.']})
chapter('02_ashen_covenant','02 | The Ashen Covenant','writable_book',{
 'The king\'s bargain':[
  'The oldest account says a king ended a terrible war by making a bargain with three wardens: a furnace below the world, a sovereign who could not die, and a witness beyond the last star. The crown called the bargain peace. Later ledgers called it the covenant.',
  'For a time, the granaries filled and the borders held. Then the payments changed. Ore became tribute. Tribute became names. Villages disappeared from maps before anyone admitted that their people were missing.'],
 'The broken seal':[
  'When the court fell, its promises did not fall with it. Fortresses continued their watch. Tombs continued their accounting. Something beyond the sky continued to wait. The world you inhabit is living among orders that nobody has rescinded.',
  'The Ashen Covenant is the name survivors gave to their answer: preserve the testimony, open the roads, and refuse any peace that needs another empty village. Whether you follow the campaign or make a different history, that question remains yours to answer.']})
chapter('03_dawnstead','03 | A Light Called Dawnstead','campfire',{
 'A settlement begins':[
  'Dawnstead is not a dot an explorer can simply find on a map. It is the name the letters give to a refuge that chooses to remain. Your version might be a stone keep, a fishing village, a colony on a hill, or a ring of cabins around one stubborn fire.',
  'Rowan keeps the relief ledger. Mara repairs the tools nobody sings about. Elian teaches the watch to learn a traveller\'s name before demanding their business. Sera saves ordinary recipes beside forbidden histories. Tamsin counts medicine; Fen counts people aboard his boat twice.'],
 'The work of the living':[
  'Their voices belong to the chronicle. Their work belongs to you. Feed a village, recruit companions, build a safe crossing, or bring somebody home from a dangerous expedition. Each is a way of making the Deep Night smaller.',
  'A settlement becomes real through the things you build and the people your installed mods provide. The book promises no pre-built Dawnstead and summons none of its letter writers. You decide where its light belongs.']})
chapter('04_road_between_worlds','04 | The Roads Between Worlds','compass',{
 'The atlas of thresholds':[
  'Sera once drew the worlds as floors of a tower: the Overworld above the Nether, the End somewhere beyond both. Then a door opened into a room that should have occupied the same space as her desk. She tore out the diagram.',
  'The newer atlas resembles a knot. Some realms are immense, others are constructed passages or private chambers. Some crossings lead to another landscape; others lead to a trial. A familiar sky is not proof that you have returned home.'],
 'An explorer\'s covenant':[
  'Write down the threshold you used. Leave provisions where you can recover them. Tell your companions which side should wait. The finest map in the world is of little use if its owner mistakes a way in for a way out.',
  'The dimension pages in this section follow the realms registered by your current world or server. A realm may be a technical workspace or lack a Survival entrance. Its presence in the atlas is not a portal recipe; the owning mod determines access.']})
chapter('05_scales_and_oaths','05 | Scales and Oaths','dragon_breath',{
 'Beyond the hunting ledger':[
  'Ysolde keeps two books. One records the creatures that threatened the roads and the expeditions sent against them. The other records the creatures that returned beside those expeditions. She refuses to bind the books together.',
  'A dragon may be a terror, a companion, a young creature defending itself, or something older than the kingdom that named it a monster. Wings and scales alone do not tell you which.'],
 'Trust and victory':[
  'The peoples of Dawnstead learned to distinguish taming from conquest. A bond earned through patience cannot stand in for a battle won; a trophy cannot stand in for trust.',
  'Dragonfire, FantasyCraft, Saints Dragons and other creature mods follow their own rules. The guide\'s taming and boss checklists record separate achievements. Read the creature\'s actual instructions before offering food, drawing a weapon, or climbing into a saddle.']})
chapter('06_elemental_memory','06 | The Memory in the Elements','amethyst_shard',{
 'Six voices':[
  'The college teaches that fire remembers motion, earth remembers weight, water remembers every shape it has abandoned, and air remembers the spaces between things. Lightning remembers the instant of division. Spirit remembers the question that remained afterward.',
  'Mara dislikes the lecture. She asks whether the staff will survive rain. Sera says both questions deserve an answer. Their argument has become the first lesson of the reopened college.'],
 'Power with a purpose':[
  'An elemental essence is useful because somebody learned how to give power a form. What matters afterward is where you aim it. A weapon that clears a road and a weapon that burns a refuge can begin on the same crafting table.',
  'These are the college\'s stories about magic, not extra elemental mechanics. The Elemental Staffs pages contain the actual crafting steps, controls and cooldowns for the supplied Java port.']})
chapter('07_keepers_of_the_night','07 | The Keepers of the Night','nether_star',{
 'Orders that outlived their makers':[
  'Some ruins contain a treasure. Others contain a command. The armoured sentinel still guards the empty room; the furnace still demands fuel; the sovereign still expects the dead to kneel. The great encounters of the Deep Night are the places where those old commands have acquired bodies.',
  'Call them bosses if you need a heading for the expedition ledger. A heading is useful. It will not stop the first blow. Learn the approach, study the arena, and prepare a retreat before you decide that a name is ready to be crossed out.'],
 'The record after the battle':[
  'The boss checklist records victories, not virtue. Its automatic marks come from kills credited to you by the game. A companion\'s final strike, a scripted ending, or a battle fought as a company may need a manual entry.',
  'When you return, record more than the spoils. Was the road reopened? Did the settlement survive? If nobody is safer afterward, Sera asks that you leave a little space beside the word victory.']})
chapter('08_after_the_dawn','08 | What Comes After Dawn','sunflower',{
 'An unfinished last page':[
  'The oldest heroic stories end when a throne changes hands. Rowan has begun collecting different endings: a bridge repaired, a debt forgiven, a child reaching home before sunset. The archive is running out of shelves.',
  'You may defeat every name in the hunting ledger and still find a new wilderness beyond the map. You may tame a creature nobody in Dawnstead has seen, or discover a realm the scribes thought was a metaphor. There is no shortage of things worth bringing back besides a crown.'],
 'Your account':[
  'Build a place that does not require your constant heroism to survive. Teach somebody else the route. Leave enough food for the next traveller. Then choose the story you want the world to remember when it finally stops calling this age the Deep Night.']})

dimension('minecraft:overworld','The Overworld | The Ash Road','grass_block',[
 'This is the world of rain on roofs, bread cooling by a window, and ruins whose stones have been borrowed to build somebody else\'s home. Empires wrote their borders across it. Forests and rivers have been editing those borders ever since.',
 'The Ash Road is the chronicle\'s name for the fragile thread between inhabited places. Each safe campsite adds a little length to it. Every abandoned watchtower asks whether you will pass by, rebuild, or find a different route.'],['The Overworld is your main surface world. Installed biome, structure and creature mods shape its terrain and inhabitants. Story names such as the Ash Road and Dawnstead are not fixed coordinates.'])
dimension('minecraft:the_nether','The Nether | Beneath the Cinder Gate','netherrack',[
 'The first heat through the gate feels like a door opening into a forge. Then comes the scale of the place: seas of fire, impossible heights, and fortresses built for a war that had no need of daylight.',
 'In the covenant accounts, the Nether is where promises were melted into obligations. Mara calls that a poet\'s explanation for a foundry. She packs fire protection before continuing the argument.'],['In ordinary Minecraft, a lit obsidian portal connects the Overworld and Nether. Mods and server rules may alter progression. Beds explode here. Keep a protected return portal and prepare for lava.', 'Incendium and other installed world-generation mods may transform this existing dimension; they do not necessarily add a separate dimension to the atlas.'])
dimension('minecraft:the_end','The End | Beyond the Last Star','end_stone',[
 'The End does not greet an expedition. It permits one to stand, briefly, upon stone above an absence. The first explorers described its islands as the remains of a sky that had forgotten what belonged beneath it.',
 'Sera believes the last witness of the covenant waited here because no kingdom could surround it with enough people to make a lie sound true. Fen suggests a simpler reason: nobody wanted visitors.'],['The End is a separate dimension reached through an activated End portal under ordinary rules. End Remastered may change the required eyes and progression. Consult its current recipes and configuration. The void remains dangerous, and beds explode here.'])
dimension('the_afterdark:afterdark','The Afterdark | The Other Night','sculk',[
 'Night in the Overworld leaves promises behind it: a roof, a sunrise, a familiar trail. The travellers\' name Afterdark describes the fear of crossing beyond those promises.',
 'The college leaves a lantern beside this page. It is replaced whenever its flame goes out. Nobody has agreed whether the custom is a precaution or an apology.'])
dimension('dragonforged:gransys','Gransys | A Different Inheritance','dragon_egg',[
 'The first fragment labelled Gransys reached the archive wrapped around a scale. Its mapmaker had drawn the mountains carefully and left the spaces between them full of questions.',
 'Ysolde warns against arriving in another world with the conviction that you already understand dragons. A creature\'s shape can cross a threshold more easily than the meaning people give it.'])
dimension('the_sift:the_sift','The Sift | What Slips Through','sand',[
 'The scribes imagine the worlds resting in a great sieve. Things too small for history fall between its threads: a lost hour, an unnamed road, the memory of a house demolished before anybody drew it.',
 'They call the place beneath that sieve the Sift. It is an explanation written by people who have misplaced too much. An explorer should carry a notebook and be willing to replace the explanation.'])
dimension('pale_monarch:sealed_realm','The Sealed Realm | A Crown Behind Glass','quartz',[
 'A seal serves two purposes: it proves who closed a thing, and it warns the next person to consider why. Every report of the Sealed Realm makes Sera turn the royal signet over in her hand.',
 'The pale crown of the chronicle is a symbol of authority cut off from the people it claims to protect. Whether the threshold holds a ruler, a prison, or a warning is a question an expedition should approach with care.'])
dimension('slayersbeasts:crypt','The Crypt | The Names Below','chiseled_stone_bricks',[
 'Elian asks every expedition below a burial place to remember that it was built for mourners before it became a destination for hunters. The names above its doors once meant somebody was expected to return and read them.',
 'In the chronicle, the crypt keeps the accounts the living were forbidden to see. Bring a light steady enough to read by, and patience enough to distinguish a warning from an invitation.'])
dimension('slayersbeasts:sepulchra','Sepulchra | The Long Remembrance','soul_lantern',[
 'The name reaches Dawnstead in a sailor\'s story and a scholar\'s footnote, neither willing to acknowledge the other. Both describe a place where remembrance has become larger than the people doing the remembering.',
 'Sera reserves several blank pages for Sepulchra. She says a realm deserves to be witnessed before it is explained.'])
dimension('dimdungeons:dungeon_dimension','Dimensional Dungeons | The Borrowed Door','chiseled_stone_bricks',[
 'Some doorways open onto the far side of a wall. Others open onto the intention of whoever built the wall. The archive calls these constructed passages: space arranged into an argument, with an explorer expected to provide the answer.',
 'Mara respects a well-built dungeon less than its architect might hope. She says a door should explain where it leads.'],['This is Dimensional Dungeons\' registered dungeon realm. Use that mod\'s own portal and key instructions for access; the atlas does not supply or consume a key.'])
dimension('dimdungeons:build_dimension','The Builder\'s Annex | Behind the Walls','structure_block',[
 'On the reverse of an ancient floor plan, somebody has drawn a workshop instead of another chamber. The chronicle leaves it there: a reminder that even an impossible labyrinth was once somebody\'s work.'],['This is Dimensional Dungeons\' build dimension, a technical realm associated with constructing dungeon content. It is listed because your server registers it, not as a promised Survival destination.'])
dimension('dungeonz:dungeon','The Dungeon | A Map with Edges','iron_door',[
 'Iona distinguishes a wilderness from a trial. A wilderness does not care whether you understand it. A trial has been arranged by somebody who does.',
 'A doorway into a dungeon is therefore also a meeting with an absent designer. Look at what the route asks you to carry, risk, and leave behind.'])
dimension('irons_spellbooks:pocket_dimension','The Pocket Realm | A Fold in the Page','enchanted_book',[
 'The first lesson in folding space was an accident involving an overfilled archive and a desk that refused to fit through its own door. At least, that is the version Mara insists the college print.',
 'Sera prefers to say that a small space can hold an unreasonable amount of intention. Both agree that the exit should be labelled clearly.'],['This is a pocket dimension registered by Iron\'s Spells \'n Spellbooks. Its availability and access follow that mod\'s mechanics; registration is not evidence of a normal overworld portal.'])
dimension('scorched_earth:scorched_k','The Scorched Kingdom | An Ashen Standard','black_banner',[
 'A blackened standard hangs in the archive without an inscription. The cloth survived its kingdom\'s account of itself. Elian studies the stitching instead of guessing which army deserved the victory.',
 'The chronicle places this relic beside the Scorched Kingdom: a story about what a border looks like after the people arguing over it have left.'])
dimension('threateningly_mobs:distortion_dimension','The Distortion | The Unreliable Compass','recovery_compass',[
 'The expedition brought back three maps. Each marked the same doorway on a different side of the room. Iona kept all three. Disagreement, she said, was the first reliable thing they had recorded.',
 'The Distortion becomes a warning in the chronicle: certainty is equipment, and equipment can fail. Make a return plan that does not depend on your first impression being correct.'])
dimension('vtubruhlotrmobs:isengard','Isengard | The Distant Tower','stone_bricks',[
 'A name from another tradition enters the atlas. The Dawnstead scribes leave its older stories intact rather than appointing their own king to a place they have barely begun to understand.',
 'For the chronicle, Isengard is a reminder that crossing worlds means crossing histories. Arrive as an observer before deciding that every distant tower belongs in the covenant\'s account.'])
dimension('bb:realend','The Real End | A Second Last Page','ender_eye',[
 'Somebody has written REAL beside the word END on the oldest chart. Sera has not crossed out either word. She says travellers are often certain of an ending immediately before finding another road.',
 'The chronicle leaves this second last page unfinished. A name alone cannot tell you whether you have reached a deeper truth or somebody else\'s persuasive story.'])

bosses={}
def boss(entity,notes=None):
    bosses['entity:'+entity]={'boss':True,'sections':{'Boss record':['Included in the Deep Night boss ledger. Your credited kills and manual checkbox track defeat separately from taming.']}}
    if notes:bosses['entity:'+entity]['sections'].update(notes)
boss('minecraft:ender_dragon',{'Encounter':['The End\'s central-island dragon encounter. Follow your pack\'s End-portal progression. The fight is separate from taming any modded dragon.']})
boss('minecraft:wither',{'Encounter':['Under vanilla rules, build a T of soul sand or soul soil and place three wither skeleton skulls across its top. Choose an isolated arena: the Wither can destroy terrain. Pack rules may change summoning.']})
for namespace,entities in {
 'cataclysm':['ignis','netherite_monstrosity','ender_guardian','the_harbinger','the_leviathan','ancient_remnant','maledictus','scylla'],
 'bosses_of_mass_destruction':['lich','obsidilith','gauntlet','void_blossom'],
 'meetyourfight':['bellringer','dame_fortuna','swampjaw','rosalyne'],
 'irons_spellbooks':['dead_king','fire_boss'],
 'mowziesmobs':['ferrous_wroughtnaut','frostmaw','umvuthi'],
 'alexsmobs':['void_worm'],
 'pale_monarch':['pale_monarch'],
 'fantasycraft':['king_dragon','surtr','hydra_1','hydra_2','hydra_3','hydra_4','hydra_5']
}.items():
    for entity in entities:boss(namespace+':'+entity)
for entity in ['king_dragon','surtr','hydra_1','hydra_2','hydra_3','hydra_4','hydra_5']:
    bosses['entity:fantasycraft:'+entity]['sections']['Boss record']=['A major FantasyCraft encounter selected for the Deep Night boss ledger. This is the guide\'s encounter grouping; the port does not give every listed creature a boss health bar. Read its existing habitat, combat and loot notes below.']

OUT.mkdir(parents=True,exist_ok=True)
(OUT/'lore.json').write_text(json.dumps({'chapters':chapters,'dimensions':dimensions},indent=2,ensure_ascii=False),encoding='utf-8')
(OUT/'boss_entries.json').write_text(json.dumps(bosses,indent=2),encoding='utf-8')
summary={'chronicle_chapters':len(chapters),'dimension_templates':len(dimensions),'explicit_boss_entries':len(bosses),'additional_boss_detection':'Live forge/c/minecraft boss tags and boss-bar classes, with Mowzie hasBossBar adapter.'}
(ROOT/'verification/lore-content-coverage.json').write_text(json.dumps(summary,indent=2))
print(json.dumps(summary))
