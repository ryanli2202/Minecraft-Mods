from pathlib import Path
p=Path(__file__).resolve().parents[1]/'src/main/java/dev/travellerscompanions/client/RosterScreen.java'
s=p.read_text().replace('private int x,y,w,h,rail,step;', 'private int x,y,w,h,rail,step,offset,visible;')
s=s.replace('step=Math.min(55,(h-72)/CharacterKind.COUNT);','visible=Math.min(CharacterKind.COUNT,Math.max(1,(h-100)/28));offset=Math.max(0,Math.min(offset,CharacterKind.COUNT-visible));step=Math.min(43,(h-100)/visible);')
s=s.replace('for(int i=0;i<CharacterKind.COUNT;i++)','for(int i=offset;i<offset+visible;i++)').replace('y+63+i*step','y+63+(i-offset)*step')
s=s.replace('int dx=x+rail+16,dw=w-rail-32;', '''if(visible<CharacterKind.COUNT){
            var previous=addRenderableWidget(Button.builder(Component.literal("Prev"),b->{offset=Math.max(0,offset-visible);rebuildWidgets();}).bounds(x+12,y+h-27,(rail-28)/2,18).build());previous.active=offset>0;
            var next=addRenderableWidget(Button.builder(Component.literal("Next"),b->{offset=Math.min(CharacterKind.COUNT-visible,offset+visible);rebuildWidgets();}).bounds(x+16+(rail-28)/2,y+h-27,(rail-28)/2,18).build());next.active=offset+visible<CharacterKind.COUNT;
        }
        int dx=x+rail+16,dw=w-rail-32;''',1)
old='preview.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,new ItemStack(selected==0?Items.BOW:selected==CharacterKind.MARA.ordinal()?Items.CROSSBOW:selected==3?Items.TRIDENT:Items.IRON_SWORD));if(selected==2)preview.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND,new ItemStack(Items.SHIELD));'
assert old in s
s=s.replace(old,'preview.giveStarter();')
s=s.replace('private void send(int action)', '''@Override public boolean mouseScrolled(double mx,double my,double delta){if(mx>=x&&mx<x+rail&&my>=y+43&&my<y+h&&visible<CharacterKind.COUNT){offset=Math.max(0,Math.min(CharacterKind.COUNT-visible,offset+(delta<0?1:-1)));rebuildWidgets();return true;}return super.mouseScrolled(mx,my,delta);}
    private void send(int action)''')
p.write_text(s)
p=Path(__file__).resolve().parents[1]/'src/main/java/dev/travellerscompanions/qa/CompanionGameTests.java'
s=p.read_text().replace('loaded.entries.length==5','loaded.entries.length==CharacterKind.COUNT').replace('.size()==5','.size()==CharacterKind.COUNT')
p.write_text(s)
