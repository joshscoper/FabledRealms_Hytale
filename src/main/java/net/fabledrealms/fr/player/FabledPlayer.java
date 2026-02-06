package net.fabledrealms.fr.player;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.List;
import java.util.UUID;

public class FabledPlayer {

    Player player;
    PlayerRef playerRef;

    // General Stats
    int level;
    int experience;
    int kills;
    int deaths;

    // Economy
    int balance;

    // Social
    List<UUID> friends;
    List<UUID> ignored;

    // Professions
    int miningLevel;
    int miningExperience;
    int farmingLevel;
    int farmingExperience;
    int fishingLevel;
    int fishingExperience;
    int woodcuttingLevel;
    int woodcuttingExperience;
    int skinningLevel;
    int skinningExperience;
    int herbloreLevel;
    int herbloreExperience;
    int cookingLevel;
    int cookingExperience;
    int smithingLevel;
    int smithingExperience;
    int tailoringLevel;
    int tailoringExperience;
    int alchemyLevel;
    int alchemyExperience;
    int enchantingLevel;
    int enchantingExperience;
    int woodworkingLevel;
    int woodworkingExperience;

    public FabledPlayer(Player player, Boolean hasData) {
        this.player = player;
        this.playerRef = player.getPlayerRef();
    }

    private void populatePlayer(){
        //TODO populate the player's values
    }

    // Getters


    public Player getPlayer() {
        return player;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBalance() {
        return balance;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public List<UUID> getIgnored() {
        return ignored;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public int getMiningExperience() {
        return miningExperience;
    }

    public int getFarmingLevel() {
        return farmingLevel;
    }

    public int getFarmingExperience() {
        return farmingExperience;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public int getFishingExperience() {
        return fishingExperience;
    }

    public int getWoodcuttingLevel() {
        return woodcuttingLevel;
    }

    public int getWoodcuttingExperience() {
        return woodcuttingExperience;
    }

    public int getSkinningLevel() {
        return skinningLevel;
    }

    public int getSkinningExperience() {
        return skinningExperience;
    }

    public int getHerbloreLevel() {
        return herbloreLevel;
    }

    public int getHerbloreExperience() {
        return herbloreExperience;
    }

    public int getCookingLevel() {
        return cookingLevel;
    }

    public int getCookingExperience() {
        return cookingExperience;
    }

    public int getSmithingLevel() {
        return smithingLevel;
    }

    public int getSmithingExperience() {
        return smithingExperience;
    }

    public int getTailoringLevel() {
        return tailoringLevel;
    }

    public int getTailoringExperience() {
        return tailoringExperience;
    }

    public int getAlchemyLevel() {
        return alchemyLevel;
    }

    public int getAlchemyExperience() {
        return alchemyExperience;
    }

    public int getEnchantingLevel() {
        return enchantingLevel;
    }

    public int getEnchantingExperience() {
        return enchantingExperience;
    }

    public int getWoodworkingLevel() {
        return woodworkingLevel;
    }

    public int getWoodworkingExperience() {
        return woodworkingExperience;
    }

    // Setters


    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public void setIgnored(List<UUID> ignored) {
        this.ignored = ignored;
    }

    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    public void setMiningExperience(int miningExperience) {
        this.miningExperience = miningExperience;
    }

    public void setFarmingLevel(int farmingLevel) {
        this.farmingLevel = farmingLevel;
    }

    public void setFarmingExperience(int farmingExperience) {
        this.farmingExperience = farmingExperience;
    }

    public void setFishingLevel(int fishingLevel) {
        this.fishingLevel = fishingLevel;
    }

    public void setFishingExperience(int fishingExperience) {
        this.fishingExperience = fishingExperience;
    }

    public void setWoodcuttingLevel(int woodcuttingLevel) {
        this.woodcuttingLevel = woodcuttingLevel;
    }

    public void setWoodcuttingExperience(int woodcuttingExperience) {
        this.woodcuttingExperience = woodcuttingExperience;
    }

    public void setSkinningLevel(int skinningLevel) {
        this.skinningLevel = skinningLevel;
    }

    public void setSkinningExperience(int skinningExperience) {
        this.skinningExperience = skinningExperience;
    }

    public void setHerbloreLevel(int herbloreLevel) {
        this.herbloreLevel = herbloreLevel;
    }

    public void setHerbloreExperience(int herbloreExperience) {
        this.herbloreExperience = herbloreExperience;
    }

    public void setCookingLevel(int cookingLevel) {
        this.cookingLevel = cookingLevel;
    }

    public void setCookingExperience(int cookingExperience) {
        this.cookingExperience = cookingExperience;
    }

    public void setSmithingLevel(int smithingLevel) {
        this.smithingLevel = smithingLevel;
    }

    public void setSmithingExperience(int smithingExperience) {
        this.smithingExperience = smithingExperience;
    }

    public void setTailoringLevel(int tailoringLevel) {
        this.tailoringLevel = tailoringLevel;
    }

    public void setTailoringExperience(int tailoringExperience) {
        this.tailoringExperience = tailoringExperience;
    }

    public void setAlchemyLevel(int alchemyLevel) {
        this.alchemyLevel = alchemyLevel;
    }

    public void setAlchemyExperience(int alchemyExperience) {
        this.alchemyExperience = alchemyExperience;
    }

    public void setEnchantingLevel(int enchantingLevel) {
        this.enchantingLevel = enchantingLevel;
    }

    public void setEnchantingExperience(int enchantingExperience) {
        this.enchantingExperience = enchantingExperience;
    }

    public void setWoodworkingLevel(int woodworkingLevel) {
        this.woodworkingLevel = woodworkingLevel;
    }

    public void setWoodworkingExperience(int woodworkingExperience) {
        this.woodworkingExperience = woodworkingExperience;
    }

    // Helpers
    public void addFriend(UUID uuid) {
        this.friends.add(uuid);
    }
    public void addIgnored(UUID uuid) {
        this.ignored.add(uuid);
    }
    public void removeFriend(UUID uuid) {
        this.friends.remove(uuid);
    }
    public void removeIgnored(UUID uuid) {
        this.ignored.remove(uuid);
    }
}
