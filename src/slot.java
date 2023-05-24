public class slot {
    public double slotWidth;
    public double slotDepth;
    boolean isEmpty;
    int slot_num;

    public slot(){}
    public slot(double slotWidth, double slotDepth) {
        this.slotWidth = slotWidth;
        this.slotDepth = slotDepth;
    }

    //////////////////////////////////////getters
    public double getSlotDepth() {
        return slotDepth;
    }
    public double getSlotWidth() {
        return slotWidth;
    }
    public int getSlot_num() {
        return slot_num;
    }

}

