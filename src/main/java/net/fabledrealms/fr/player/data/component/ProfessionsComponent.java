package net.fabledrealms.fr.player.data.component;

public class ProfessionsComponent {

    private SkillValue mining = new SkillValue();
    private SkillValue farming = new SkillValue();
    private SkillValue fishing = new SkillValue();
    private SkillValue woodcutting = new SkillValue();
    private SkillValue skinning = new SkillValue();
    private SkillValue herblore = new SkillValue();
    private SkillValue cooking = new SkillValue();
    private SkillValue smithing = new SkillValue();
    private SkillValue tailoring = new SkillValue();
    private SkillValue alchemy = new SkillValue();
    private SkillValue enchanting = new SkillValue();
    private SkillValue woodworking = new SkillValue();

    public SkillValue getMining() { return mining; }
    public void setMining(SkillValue mining) { this.mining = mining; }
    public SkillValue getFarming() { return farming; }
    public void setFarming(SkillValue farming) { this.farming = farming; }
    public SkillValue getFishing() { return fishing; }
    public void setFishing(SkillValue fishing) { this.fishing = fishing; }
    public SkillValue getWoodcutting() { return woodcutting; }
    public void setWoodcutting(SkillValue woodcutting) { this.woodcutting = woodcutting; }
    public SkillValue getSkinning() { return skinning; }
    public void setSkinning(SkillValue skinning) { this.skinning = skinning; }
    public SkillValue getHerblore() { return herblore; }
    public void setHerblore(SkillValue herblore) { this.herblore = herblore; }
    public SkillValue getCooking() { return cooking; }
    public void setCooking(SkillValue cooking) { this.cooking = cooking; }
    public SkillValue getSmithing() { return smithing; }
    public void setSmithing(SkillValue smithing) { this.smithing = smithing; }
    public SkillValue getTailoring() { return tailoring; }
    public void setTailoring(SkillValue tailoring) { this.tailoring = tailoring; }
    public SkillValue getAlchemy() { return alchemy; }
    public void setAlchemy(SkillValue alchemy) { this.alchemy = alchemy; }
    public SkillValue getEnchanting() { return enchanting; }
    public void setEnchanting(SkillValue enchanting) { this.enchanting = enchanting; }
    public SkillValue getWoodworking() { return woodworking; }
    public void setWoodworking(SkillValue woodworking) { this.woodworking = woodworking; }

    public static class SkillValue {
        private int level = 1;
        private int experience = 0;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }
    }
}
