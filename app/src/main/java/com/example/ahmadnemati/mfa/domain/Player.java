package com.example.ahmadnemati.mfa.domain;

public class Player {
    public String getPrintableInfo() {
        return "Role: " + role.name() + "\n"
                + (isAlive ? "Alive" : "Dead") + "\n"
                + (isAngel ? "Angel" : "Zombie") + "\n"
                + "Life wishes: " + lifeWishes + "\n"
                + "Death wishes: " + deathWishes;
    }

    public enum Role {
        Mafia,
        Citizen,
        Doctor,
        Achilles,
        Dynamite,
        Lover,
        Baker
    }

    public boolean isAlive = true;
    public boolean isAngel = true;
    public String name;
    public int lifeWishes;
    public int deathWishes;
    public Role role;
    public int index;
    
    public Player(){}

    public Player(String name, Role role, int index) {
        this.index = index;
        this.name = name;
        this.role = role;
    }

    /**
     * player killed by day vote
     * if he is not mafia becomes zombie
     * and if he is mafia becomes isAngel
     * player killed by mafia becomes isAngel
     *
     * @param becomeAngel pass false for zombie and true for isAngel
     */
    public void killPlayer(boolean becomeAngel) {
        this.isAlive = false;
        this.isAngel = becomeAngel;
    }

    public Player.Role convertIntToRole(int num) {
        switch (num) {
            case 2:
                return Player.Role.Mafia;
            case 3:
                return Player.Role.Doctor;
            case 4:
                return Player.Role.Achilles;
            case 5:
                return Player.Role.Dynamite;
            case 6:
                return Player.Role.Lover;
            case 7:
                return Player.Role.Baker;
            default:
                return Player.Role.Citizen;
        }
    }

    public int convertRoleToInt(Player.Role role) {
        switch (role) {
            case Mafia:
                return 2;
            case Doctor:
                return 3;
            case Achilles:
                return 4;
            case Dynamite:
                return 5;
            case Lover:
                return 6;
            case Baker:
                return 7;
            default:
                return 1;
        }
    }
}
