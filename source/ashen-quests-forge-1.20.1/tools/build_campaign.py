"""Deterministic conversion of the user's two Boundless packs + authored Chronicles III-V and companion stories."""
import json, re, hashlib
from pathlib import Path

PROJECT = Path(__file__).resolve().parents[1]
WORK = PROJECT.parent
SOURCE = PROJECT / 'tools/reference/boundless/questpacks'
if not SOURCE.exists(): SOURCE = WORK / 'boundless/questpacks'
OUT = PROJECT / 'src/main/resources/data/ashenquests/ashen_quests/campaign.json'
quests = []
sources = []
def read(p): return json.loads(p.read_text(encoding='utf-8-sig'))
def human(s): return s.split(':')[-1].replace('_', ' ').replace('/', ' / ').title()
def objective(kind, target, count=1):
    if kind != 'field' and ':' not in target: target = 'minecraft:' + target
    verb = {'collect':'Hold', 'submit':'Donate', 'kill':'Defeat', 'achieve':'Advancement', 'field':'Write', 'effect':'Have effect', 'craft':'Craft', 'smelt':'Smelt', 'visit':'Visit dimension', 'biome':'Discover biome', 'interact':'Speak to', 'tame':'Tame', 'fish':'Catch'}[kind]
    return dict(type=kind, target=target, count=count, label=f'{verb}: {target if kind == "field" else human(target)}')

for folder in sorted(SOURCE.iterdir()):
    cats = {read(p)['id']: read(p) for p in folder.glob('data/*/quests/categories/*.json')}
    subs = {read(p)['id']: read(p) for p in folder.glob('data/*/quests/sub-category/*.json')}
    paths = sorted(folder.glob('data/*/quests/*.json'))
    paths.sort(key=lambda p: (cats.get(read(p)['category'],{}).get('order',99), subs.get(read(p).get('sub-category'),{}).get('order',99), p.name))
    for p in paths:
        old = read(p)
        sources.append(dict(path=str(Path('boundless/questpacks')/p.relative_to(SOURCE)),sha256=hashlib.sha256(p.read_bytes()).hexdigest()))
        items = old.get('reward',{}).get('items',[])
        obs = [objective(k,t[k],t.get('count',1)) for t in old['completion']['complete'] for k in ('collect','submit','kill','achieve','effect','field') if k in t]
        story = old['description'].replace('Repeat in the quest book', 'Claim in this journal after the cooldown').replace('Both books of the main campaign are complete.', 'The first two books are complete. Book III: The Hollow Concord now awaits you.')
        story = story.replace('Your tracked totals', 'Your lifetime Minecraft kill statistics').replace('your tracked totals', 'your lifetime Minecraft kill statistics')
        deep = not old['id'].startswith('ac_')
        if deep:
            story = 'From Elian\'s recovered Deep Night ledger: the threats once listed as isolated hunts now form a map of the failing border. Every completed contract gives the watch time to bring another village into the compact.\n\n' + story
        section = subs.get(old.get('sub-category'),{}).get('name','The Deep Night')
        faction = 'Dawnstead'
        if any(w in section.lower() for w in ('sera','mage','relic','library','ink','eyes','archive')): faction='Archivists'
        elif any(w in section.lower() for w in ('mara','forge','craft','armor')): faction='Hearthwrights'
        elif any(w in section.lower() for w in ('elian','knight','hunt','watch','pillager','fiend','war')): faction='Free Watch'
        elif any(w in section.lower() for w in ('iona','fen','dragon','sea','sky','coast','ranger','atlas')): faction='Wayfarers'
        emeralds = max(sum(i['count'] for i in items if i['item']=='minecraft:emerald'), 4 if old.get('repeatable') else 8 if any(o['type']=='kill' for o in obs) else 3)
        quests.append(dict(id=old['id'],title=old['name'],chapter=cats.get(old['category'],{}).get('name',old['category']),section=section,story=story,
            aftermath='',icon=old.get('icon','minecraft:book'),requires=old.get('dependencies',[]),objectives=obs,
            rewards=[i for i in items if i['item']!='minecraft:emerald'],emeralds=emeralds,xp=old.get('reward',{}).get('count',0),faction=faction,standing=0 if old.get('repeatable') else 5,cooldown=1200 if old.get('repeatable') else 0,choices=[]))

def o(k,t,n=1): return objective(k,t,n)
def add_arc(code, title, unlock, faction, rows, side=False, choices=None):
    for i,(name,story,task,targets,after) in enumerate(rows,1):
        deps=[unlock] if i==1 else [f'{code}{i-1:02}']
        if i==3: deps=[f'{code}01']
        if i==4: deps=[f'{code}02',f'{code}03']
        quests.append(dict(id=f'{code}{i:02}',title=name,chapter='Companion Chronicles | Letters Never Sent' if side else 'Book III | The Hollow Concord',section=title,
            story=story+'\n\n'+task,aftermath=after,icon='minecraft:writable_book' if side else 'minecraft:echo_shard',requires=deps,objectives=targets,rewards=[],emeralds=(8 if side else 16)+(16 if i==6 else 0),xp=60 if side else 140,faction=faction,standing=10 if i<6 else 25,cooldown=0,choices=choices if i==6 and choices else []))

add_arc('hc_signal','XV | The Signal beneath the Bells','ac_dawn08','Archivists',[
 ('A Letter with Your Handwriting','The new council receives a letter describing a disaster that has not happened. It is written in your hand. Rowan refuses to burn it; Sera refuses to believe it. Both ask you to keep the original.','Hold a writable book and an echo shard. Echo shards can be found in ancient-city chests; you keep the evidence.',[o('collect','writable_book'),o('collect','echo_shard')],'Under lamplight, a second sentence becomes visible: do not let the bells agree.'),
 ('The Listeners at the Window','Every bell in Dawnstead sounds a fraction late. Sera thinks an old signal network is answering the village instead of echoing it.','Craft two note blocks. Crafting is recorded from installation onward, including output quantity.',[o('craft','note_block',2)],'The two notes produce three answers. Something is learning the interval.'),
 ('A Refuge for the Sleepless','Tamsin treats people who have dreamed the same empty street. A crowded hall frightens them more than a locked door, so she asks for rooms with separate lamps.','Donate 16 white wool and 8 lanterns. This branch can be completed before or after the listening experiment.',[o('submit','white_wool',16),o('submit','lantern',8)],'For the first time in a week, the hall sleeps. One patient leaves a drawing of a city beneath the roots.'),
 ('The City That Answers','The letter describes a place where silence is engineered. Sera marks the deep dark on your map, but refuses to send a novice alone.','Enter a deep dark biome. Discovery remains recorded after you leave; defeating a warden is not required.',[o('biome','deep_dark')],'Your footsteps return as the sound of a crowd. The city remembers people, not merely noise.'),
 ('A Frequency in Stone','Mara recognizes the rhythm as a forge signal used before the king was born. The machinery was built to warn the provinces together. Someone has changed what a warning means.','Hold a calibrated sculk sensor and 8 amethyst shards.',[o('collect','calibrated_sculk_sensor'),o('collect','amethyst_shard',8)],'The sensor reveals an instruction hidden beneath the alarm: assemble and surrender judgment.'),
 ('Publish the Impossible','Rowan wants to tell the council at once. Elian fears panic; Sera fears the habit of secrecy more. They agree the evidence must survive either decision.','Write "a warning is not an order", then choose how the first report is delivered.',[o('field','a warning is not an order')],'The warning travels under your seal. The investigation belongs to the provinces now.')
],choices=[dict(title='Open council hearing',result='Rowan reads the report in the square. Frightened citizens begin sending their own accounts; the archive fills with witnesses.'),dict(title='Send local witnesses',result='Elian escorts witnesses to each settlement. Rumor moves faster, but every explanation arrives with a person who can answer questions.')])

add_arc('hc_ledger','XVI | The People in the Machine','hc_signal06','Dawnstead',[
 ('An Account That Never Closes','Sera names the buried network the Hollow Concord. It once balanced food between provinces; its oldest accounts list names beside quantities of heat.','Hold 16 paper and a recovery compass.',[o('collect','paper',16),o('collect','recovery_compass')],'The compass turns toward the archive. Its needle cannot distinguish a lost person from a stored memory.'),
 ('Meals outside the Ledger','Tamsin notices that the signal weakens wherever people share food without recording a debt. The machine cannot price a gift it never sees.','Donate 32 bread and 16 pumpkin pies.',[o('submit','bread',32),o('submit','pumpkin_pie',16)],'The kitchen tears up its debt slips. Several names disappear from the machine\'s newest account.'),
 ('The Witness Who Trades','A trader remembers a royal assessor buying bells from every village. He will talk to someone who approaches without a drawn weapon.','Right-click a villager. This records the conversation; ordinary villager trading remains available.',[o('interact','villager')],'He describes an assessor with no shadow. Her assistants called her the Auditor.'),
 ('A Map of Quiet Places','Iona suspects the network follows settlements, not borders. She needs a refuge the court never found.','Discover a mushroom fields biome. An ocean expedition may be necessary.',[o('biome','mushroom_fields')],'The signal vanishes among the mushrooms. Iona adds a safe meeting place to the atlas.'),
 ('Names Are Not Numbers','Sera decodes a folio and finds no soldiers, just the names of people the court considered replaceable. Mara recognizes her mother\'s signature beneath a refusal.','Donate 8 books and 16 glow ink sacs to preserve the testimony.',[o('submit','book',8),o('submit','glow_ink_sac',16)],'Mara asks for a copy without the royal seal. She wants to remember a person, not a case.'),
 ('The Right to Be Missing','Rowan writes the first law the Concord cannot understand: no person owes the state an account of every moment of their life.','Write "no life belongs to a ledger".',[o('field','no life belongs to a ledger')],'Across the provinces, blank pages are bound into the public registers. The Auditor sends her first reply: correction pending.')
])

add_arc('hc_forge','XVII | A Bell with No Master','hc_ledger06','Hearthwrights',[
 ('Mara Opens the Old Chest','Mara has kept the tools her mother used to build the first signal relay. She offers them without asking to lead the expedition. "Let me finish the part she refused to finish."','Hold an anvil and 16 copper ingots.',[o('collect','anvil'),o('collect','copper_ingot',16)],'Inside the tool roll is a drawing of a bell that listens before it rings.'),
 ('Copper for the Listening Ring','A royal relay speaks in a single voice. Mara wants many imperfect pieces, joined where anyone can see the seams.','Smelt 24 copper ingots and take them from a furnace or blast furnace. Hopper extraction does not count.',[o('smelt','copper_ingot',24)],'The seams remain visible. Mara refuses to polish them away.'),
 ('Hands beyond the Forge','The workshop cannot keep its workers alive on philosophy. Tamsin asks for food before another night of experiments.','Donate 24 baked potatoes and 8 honey bottles.',[o('submit','baked_potato',24),o('submit','honey_bottle',8)],'The apprentices bring their families to supper. Someone starts singing off-key; the relay fails to predict it.'),
 ('A Circuit That Can Say No','Sera designs a delay that allows a settlement to reject a command. Mara insists the switch must be within reach of the smallest apprentice.','Craft 8 repeaters and 4 comparators.',[o('craft','repeater',8),o('craft','comparator',4)],'The test signal arrives, waits, and dies unanswered. Everyone in the workshop cheers.'),
 ('The Last Royal Standard','Elian brings an intact royal signal head. Its frame is valuable, but Mara will not let its ownership mark survive in the new apparatus.','Donate 4 gold ingots and 16 iron ingots to recast the fittings.',[o('submit','gold_ingot',4),o('submit','iron_ingot',16)],'The crown melts into a hinge. Nothing breaks when the king\'s name disappears.'),
 ('The Uncommanded Bell','The prototype can either warn every settlement immediately or wait for a second witness. Both choices carry a cost; Mara asks you to name it in the record.','Hold a bell and choose the relay protocol. The journal records your decision; it does not alter placed bell blocks.',[o('collect','bell')],'The new bell leaves the workshop wrapped in an ordinary blanket.')
],choices=[dict(title='Warn at the first sign',result='Warnings travel quickly, with uncertainty stated aloud. Elian trains the watch to investigate before acting.'),dict(title='Require two witnesses',result='The provinces accept a slower alarm in exchange for corroboration. Iona recruits couriers so isolated villages are never left waiting.')])

add_arc('hc_crossing','XVIII | The Road between Seconds','hc_forge06','Wayfarers',[
 ('Where the Couriers Disappear','Iona finds a route marked on no map: messages arrive before their messengers leave. The Hollow Concord has been routing its commands through the Nether.','Enter the Nether after installing Chronicles. A recorded visit remains valid.',[o('visit','the_nether')],'A distant ringing follows you through the portal. The sound stops when you turn to listen.'),
 ('A Safe Return Is a Plan','Fen offers boats for the surface leg and insists on spare provisions for anyone who turns back. Nobody earns a place by proving they can suffer.','Donate 32 cooked cod and 16 torches.',[o('submit','cooked_cod',32),o('submit','torch',16)],'The first courier chooses to return home. Iona strikes the word deserter from the old route book.'),
 ('Glass for the Lost Horizon','Sera needs lenses that can show a signal crossing the boundary between worlds. Mara asks for clear glass and promises no miraculous weapon.','Smelt 32 glass and retrieve it yourself.',[o('smelt','glass',32)],'Through the lenses the signal resembles handwriting, corrected over and over until no author remains.'),
 ('Under a Sky of Basalt','The oldest relay is buried beneath black volcanic pillars. Iona marks a route that avoids building a road through anyone\'s home.','Discover a basalt deltas biome.',[o('biome','basalt_deltas')],'The stone repeats the warning from the first letter, in your voice this time.'),
 ('The Heat of a Thousand Orders','The relay is guarded by the same creatures the court once harvested for power. Elian asks for a clear route while Sera copies the pattern.','Reach 12 lifetime blaze kills and hold 8 obsidian.',[o('kill','blaze',12),o('collect','obsidian',8)],'With the pattern copied, the expedition returns. No one is asked to stay behind to buy time.'),
 ('A Return Address','The message can finally be answered. Iona wants the reply signed by every province, not by a new savior speaking for them.','Write "we answer in our own voices".',[o('field','we answer in our own voices')],'The Concord answers with coordinates made of names. The Auditor is waiting at the far end of the old road.')
])

add_arc('hc_auditor','XIX | The Auditor without a Shadow','hc_crossing06','Free Watch',[
 ('An Audience without a Throne','The Auditor is a chorus of recorded officials, each correcting the next. It offers perfect peace in exchange for the right to decide what every person needs.','Enter the End. This encounter is told through the journal; no new boss is spawned.',[o('visit','the_end')],'Sera realizes the chorus cannot imagine a promise freely kept. It recognizes only instructions and failures.'),
 ('The Guard Who Chose to Leave','Elian remembers the order that abandoned his regiment. The Auditor calls that decision efficient. He asks you to give the survivors a different account.','Donate 8 shields for the independent watch. Items are consumed only when this reward is claimed.',[o('submit','shield',8)],'Each shield bears a settlement\'s mark. None bears a crown.'),
 ('Evidence That Cannot Be Spent','Mara brings her mother\'s refusal. Tamsin brings patients\' letters. Rowan brings the blank pages that terrified the royal clerks.','Hold a written record: 3 writable books and 4 echo shards.',[o('collect','writable_book',3),o('collect','echo_shard',4)],'The chorus cannot reconcile the testimonies. For the first time, it asks a question without supplying the answer.'),
 ('The Silence between Instructions','Sera proposes leaving a pause in the network, long enough for a listener to choose. The Auditor calls the pause an error.','Craft a daylight detector and 4 redstone lamps.',[o('craft','daylight_detector'),o('craft','redstone_lamp',4)],'One voice in the chorus falls silent voluntarily. The others cannot order it to resume.'),
 ('A Watch against Certainty','Elian expects the last signal to draw the dead along the old roads. He asks for preparation rather than a final glorious charge.','Reach 40 lifetime skeleton kills and 40 lifetime zombie kills.',[o('kill','skeleton',40),o('kill','zombie',40)],'The watch returns in groups, carrying its wounded. Elian counts the living before asking whether the road is clear.'),
 ('What We Owe the Voices','The Auditor can be preserved as an archive that cannot issue orders, or allowed to fall silent. The witnesses disagree, but they refuse to force unanimity.','Write "memory is not authority", then decide the fate of the chorus.',[o('field','memory is not authority')],'The command channel closes. Whatever remains must ask permission to be heard.')
],choices=[dict(title='Keep a listening archive',result='Sera preserves the voices behind a public switch. Descendants may listen, answer, or walk away; no voice can command them.'),dict(title='Let the chorus rest',result='Rowan reads every recoverable name before the relay goes quiet. The testimonies survive in books, with room for disagreement in the margins.')])

add_arc('hc_home','XX | The Ordinary Dawn','hc_auditor06','Dawnstead',[
 ('No Parade Today','The council has ordered a parade. Rowan cancels it when a farmer asks who will bring in the harvest. For once, the urgent work is ordinary.','Donate 64 wheat and 32 carrots.',[o('submit','wheat',64),o('submit','carrot',32)],'The banners become tablecloths. Nobody complains about the missing speeches.'),
 ('A School with Open Doors','Sera wants a school where students can correct the books. She asks Mara to make desks strong enough to survive generations of impatient questions.','Craft 8 bookshelves and 2 lecterns.',[o('craft','bookshelf',8),o('craft','lectern',2)],'The first lesson begins with a crossed-out sentence from the old royal history.'),
 ('A Harbor without Chains','Fen returns the emergency vessels to their crews. A free harbor needs stores anyone can draw on when weather turns against them.','Donate 32 dried kelp and 16 barrels.',[o('submit','dried_kelp',32),o('submit','barrel',16)],'The harbor ledger records what was taken, but leaves the debtor column out entirely.'),
 ('The Last Patrol of the War','Elian takes down the wartime watch bill. Tomorrow\'s patrol will ask travelers whether they need help before asking where they came from.','Right-click a villager and hold a compass.',[o('interact','villager'),o('collect','compass')],'The first traveler asks for directions to the school. Elian walks them there himself.'),
 ('A Light Someone Else Lit','Iona finds a lantern burning at a crossing you have never visited. The road has grown beyond the people who founded it.','Craft 16 lanterns for the next generation of wayfarers.',[o('craft','lantern',16)],'You leave a few lanterns unclaimed in the workshop. Someone else will know where they are needed.'),
 ('The Chronicle Stays Open','Rowan returns the letter from the ashes. Beneath every correction, every refusal and every impossible promise, its first request is still legible: bring a light.','Write "the dawn belongs to everyone" to close Book III. Side stories and relief contracts remain open.',[o('field','the dawn belongs to everyone')],'THE HOLLOW CONCORD: COMPLETE. No crown changes hands. No voice speaks for everyone. In the square, a bell rings because someone chose to ring it, and a city wakes to a day it is free to shape.')
])

add_arc('lc_mara','Mara | The Apprentice\'s Name','ac_m06','Hearthwrights',[
 ('An Unsigned Hammer','An apprentice leaves a repaired hammer outside Mara\'s door without a name. She recognizes the careful work of someone taught to expect rejection.','Craft an iron pickaxe.',[o('craft','iron_pickaxe')],'Mara hangs the hammer on the public tool rack instead of keeping it.'),
 ('A Place at the Bench','The workshop has tools but too few work surfaces. Mara wants a bench anyone may use without asking whose child they are.','Donate 32 oak planks and 8 iron ingots.',[o('submit','oak_planks',32),o('submit','iron_ingot',8)],'The first apprentice arrives before dawn and leaves the bench cleaner than they found it.'),
 ('Learning the Heat','A novice ruins a batch of iron and tries to repay the loss with their supper money. Mara sends them to eat and asks you for a demonstration.','Smelt 16 iron ingots and retrieve them yourself.',[o('smelt','iron_ingot',16)],'The next batch succeeds. Mara records the failed batch as tuition, not debt.'),
 ('The Maker\'s Mark','The apprentices ask whose mark to stamp on their work. Mara hands each of them a different punch.','Craft a smithing table.',[o('craft','smithing_table')],'For the first time, the workshop sells tools that do not carry Mara\'s name.'),
 ('The Work That Matters','A noble commissions a ceremonial sword. The same morning, the granary door breaks. Mara asks which job the apprentices should learn from first.','Donate 4 iron doors and 8 iron ingots to the refuge.',[o('submit','iron_door',4),o('submit','iron_ingot',8)],'The grain stays dry. The noble discovers that a sword can wait.'),
 ('A Name beside Her Own','The unsigned apprentice finally signs a piece of work. Mara notices that her own hands no longer shake when she lets someone else finish a job.','Write "no hand works alone".',[o('field','no hand works alone')],'Mara adds a second name above the workshop door, then leaves room for more.')
],side=True)

add_arc('lc_tamsin','Tamsin | A Garden for the Living','ac_m06','Dawnstead',[
 ('The Healer\'s Empty Cup','Tamsin has been giving away her own meals. Rowan asks you to help without turning her kindness into another public obligation.','Donate 8 mushroom stew.',[o('submit','mushroom_stew',8)],'Someone leaves a full cup on Tamsin\'s desk. This time she drinks it.'),
 ('Seeds with No Owner','The gardens were once divided by the families who could pay for a fence. Tamsin proposes a shared bed beside the dispensary.','Donate 32 wheat seeds and 8 beetroot seeds.',[o('submit','wheat_seeds',32),o('submit','beetroot_seeds',8)],'A child plants the first row crooked. Nobody makes them pull it out.'),
 ('A Companion at the Door','A stray wolf waits outside the dispensary every night. Tamsin thinks the animal has chosen a person who has not yet learned to accept company.','Tame a wolf after installing Chronicles.',[o('tame','wolf')],'The wolf sleeps beneath a waiting-room bench. Patients begin arriving a little less afraid.'),
 ('Bottles for Tomorrow','Tamsin wants to train others before the next emergency. The lesson begins with washing equipment, which disappoints the aspiring miracle workers.','Craft 24 glass bottles.',[o('craft','glass_bottle',24)],'By evening the apprentices can prepare a room without waiting for her instructions.'),
 ('Permission to Rest','The new healers arrange a day off for Tamsin. She objects until they show her a complete supply cupboard.','Donate 16 honey bottles and 16 apples.',[o('submit','honey_bottle',16),o('submit','apple',16)],'Tamsin spends the afternoon in the garden. Nothing terrible happens while she is absent.'),
 ('A Garden Is Not a Monument','The council offers to name the garden after Tamsin. She asks for a bench in the shade and enough room for another row of beans.','Write "care is work we share".',[o('field','care is work we share')],'The sign reads Common Garden. Tamsin sits beneath it with an empty cup and no intention of getting up yet.')
],side=True)

add_arc('lc_iona','Iona | The Map of Returning','ac_m06','Wayfarers',[
 ('The Blank Corner','Iona has mapped hundreds of roads but left the valley where she grew up blank. She says there is nothing worth visiting there.','Craft a compass and hold 8 paper.',[o('craft','compass'),o('collect','paper',8)],'A narrow track appears in the corner of the map, drawn in a different ink.'),
 ('A Pack for Two','Iona agrees to travel if nobody calls the journey a pilgrimage. Fen helps pack supplies without asking what happened in the valley.','Donate 16 bread and 8 leather.',[o('submit','bread',16),o('submit','leather',8)],'The second pack is for anyone you meet who wants to come back with you.'),
 ('The Shape of Home','The first familiar trees stop Iona mid-sentence. She points out landmarks that no military map ever bothered to name.','Discover a meadow biome.',[o('biome','meadow')],'The place has changed enough to hurt, and enough to keep living.'),
 ('The People Who Stayed','Iona expected graves. Instead she finds people who remember her and have stories that continued after she left.','Right-click a villager.',[o('interact','villager')],'Someone asks whether she still gets lost on straight roads. She laughs before she remembers to be ashamed.'),
 ('A Road in Both Directions','The valley needs trade, but Iona refuses to make belonging conditional on departure. Supplies will travel both ways.','Donate 16 rails and 8 powered rails.',[o('submit','rail',16),o('submit','powered_rail',8)],'Fen redraws the route with two arrows. Iona insists on keeping both.'),
 ('The Place She Can Return To','Iona fills the blank corner at last. There is no grand revelation in it, just a road, a well, and the names of people she can visit.','Write "leaving is not forgetting".',[o('field','leaving is not forgetting')],'She gives Rowan a copy of the atlas and keeps the version with muddy fingerprints.')
],side=True)

add_arc('lc_elian','Elian | Orders We Refuse','ac_m06','Free Watch',[
 ('The Captain\'s Sealed Report','Elian writes the same report every night and burns it before morning. Mara asks you to give him a reason to finish one.','Hold a writable book and a shield.',[o('collect','writable_book'),o('collect','shield')],'He writes the names of the abandoned regiment before writing his own rank.'),
 ('An Ordinary Watch','New recruits want stories of heroic charges. Elian assigns them a kitchen watch and asks for practical equipment.','Craft 8 torches and 2 shields.',[o('craft','torch',8),o('craft','shield',2)],'The recruits learn that keeping someone safe usually means noticing what they need.'),
 ('The Families at the Gate','The missing soldiers\' families arrive for answers. Rowan refuses to make them wait until the captain is ready to speak.','Donate 16 bread and 16 white wool.',[o('submit','bread',16),o('submit','white_wool',16)],'Elian meets them without armor. Some listen; some leave. He does not ask for forgiveness.'),
 ('A Patrol That Comes Back','The recruits\' first assignment is to clear the road and return together. Elian refuses the old measure of success: how much ground was gained.','Reach 12 lifetime zombie kills.',[o('kill','zombie',12)],'Every recruit returns. The report lists names before numbers.'),
 ('The Right to Question','A recruit challenges an unsafe order in front of the entire watch. Elian pauses, then asks them to explain their alternative.','Donate 8 books for the new watch charter.',[o('submit','book',8)],'The charter gives every recruit the right to ask why, and every captain the duty to answer.'),
 ('A Signature without a Rank','Elian finally sends the report. He keeps a copy where the new watch can read it. The last line is an account of what he will do next.','Write "an order does not erase a choice".',[o('field','an order does not erase a choice')],'He signs Elian. Beneath the name, he leaves a space for corrections.')
],side=True)

contracts=[
 ('The Night Kitchen','Dawnstead','bread',24,6,'No traveler should be turned away because they arrived after supper.'),
 ('Roofing before the Rain','Hearthwrights','spruce_planks',64,6,'Mara asks for boards before the weather turns a small repair into a lost home.'),
 ('Ink for Unheard Voices','Archivists','ink_sac',16,7,'Sera reserves a shelf for accounts that disagree with the official chronicle.'),
 ('Lanterns at the Crossing','Wayfarers','lantern',12,10,'Iona replaces the lamps on the safest route through the marsh.'),
 ('Boots by the Barracks','Free Watch','leather_boots',4,8,'The watch has enough swords and too many people walking in wet socks.'),
 ('The Harbor Pantry','Wayfarers','cooked_cod',32,8,'Fen stocks the emergency boats before the next storm warning.'),
 ('Winter Bedding','Dawnstead','white_wool',32,8,'Tamsin wants clean blankets ready before anyone needs them.'),
 ('Glass for the School','Archivists','glass',48,8,'The school needs windows more urgently than a statue of its founder.'),
 ('Hinges for Open Doors','Hearthwrights','iron_ingot',16,10,'Mara turns iron into hinges for doors that should no longer be barred.'),
 ('Seeds for New Neighbors','Dawnstead','wheat_seeds',64,5,'A newly arrived family wants to plant something before asking for anything else.'),
 ('Arrows for the Road Watch','Free Watch','arrow',48,8,'Elian keeps the watch supplied so patrols can choose distance over a reckless charge.'),
 ('Paper for the Next Map','Wayfarers','paper',48,7,'The atlas needs another page. Iona considers this the best kind of problem.')]
for i,(title,faction,item,count,pay,story) in enumerate(contracts,1):
    quests.append(dict(id=f'hc_contract{i:02}',title=title,chapter='The Notice Board | Provincial Contracts',section='Renewable relief work',story=story+'\n\nDonate the listed supplies to claim emeralds. This contract renews after 20 minutes of world play time.',aftermath='The shipment is recorded. The next request will be ready after the cooldown.',icon='minecraft:paper',requires=['ac_m06'],objectives=[o('submit',item,count)],rewards=[],emeralds=pay,xp=20,faction=faction,standing=0,cooldown=1200,choices=[]))

# The new continuing story is a deliberate sequel, while companion routes open early.
from quest_givers import assign_givers
giver_counts = assign_givers(quests)
from conversation_expansion import expand_campaign
expansion_report = expand_campaign(quests)
from eye_progression import expand_eyes
expand_eyes(quests)
from collections import Counter
giver_counts = dict(Counter(q['giver'] for q in quests))
ids={q['id'] for q in quests}
assert len(ids)==len(quests)==573
assert all(q['emeralds']>0 and set(q['requires']) <= ids for q in quests)
OUT.write_text(json.dumps(dict(schema=3,title='The Ashen Covenant: Chronicles',quests=quests),ensure_ascii=False,indent=2),encoding='utf-8')
(PROJECT/'verification/conversation-expansion.json').write_text(json.dumps(expansion_report,indent=2),encoding='utf-8')
(PROJECT/'verification/quest-givers.json').write_text(json.dumps(dict(counts=giver_counts,quests={q['id']:q['giver'] for q in quests}),indent=2),encoding='utf-8')
(PROJECT/'verification/source-manifest.json').write_text(json.dumps(sources,indent=2),encoding='utf-8')
report=dict(total=len(quests),imported=len(sources),new=len(quests)-len(sources),repeatable=sum(q['cooldown']>0 for q in quests),choices=sum(bool(q['choices']) for q in quests),emeralds_every_quest=all(q['emeralds']>0 for q in quests),objective_types=sorted({o['type'] for q in quests for o in q['objectives']}),chapters={c:sum(q['chapter']==c for q in quests) for c in dict.fromkeys(q['chapter'] for q in quests)})
(PROJECT/'verification/content-audit.json').write_text(json.dumps(report,indent=2),encoding='utf-8')
print(json.dumps(report,indent=2))

