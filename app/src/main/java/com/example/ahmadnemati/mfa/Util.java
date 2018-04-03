package com.example.ahmadnemati.mfa;

import android.util.Log;

import com.example.ahmadnemati.mfa.domain.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ahmad.nemati on 2/27/18.
 */

public class Util {

    public static String namesToString(ArrayList<String> names) {
        if (names == null) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : names) {
            Log.i("zzzzz", s);
            s.replace(",", " ");
            sb.append(s);
            sb.append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        Log.w("zzzzz", sb.toString());
        return sb.toString();
    }

    public static ArrayList<String> namesToArrayList(String names) {
        Log.d("zzzzz", names);
        String[] array = names.split(",");
        return new ArrayList<>(Arrays.asList(array));
    }


    /** set number of mafia and other roles based on people count
     * @param playerCount
     * @return
     */
    public static ArrayList<Player.Role> getRoles(int playerCount) {
        ArrayList<Player.Role> roles = new ArrayList<>();
        roles.add(Player.Role.Mafia);
        roles.add(Player.Role.Mafia);
        roles.add(Player.Role.Doctor);
        roles.add(Player.Role.Achilles);
        int remainder;
        if (playerCount < 9) {
            // 2 mafia, Dr, Ach
            remainder = playerCount - 4;
        } else if (playerCount < 11) {
            // 2 mafia, Dr, Ach, Dynamite
            roles.add(Player.Role.Dynamite);
            remainder = playerCount - 5;
        } else if (playerCount < 14) {
            // 3 mafia, Dr, Ach, Dynamite
            roles.add(Player.Role.Dynamite);
            roles.add(Player.Role.Mafia);
            remainder = playerCount - 6;
        } else {
            // 3 mafia, Dr, Ach, Dynamite, Lovers, Bakery
            roles.add(Player.Role.Dynamite);
            roles.add(Player.Role.Mafia);
            roles.add(Player.Role.Lover);
            roles.add(Player.Role.Lover);
            roles.add(Player.Role.Baker);
            remainder = playerCount - 9;
        }
        for (int i = 0; i< remainder; i++) {
            roles.add(Player.Role.Citizen);
        }
        return roles;
    }

    public static int getPlayerIndex(Player player) {
        PreferenceHelper prefs = PreferenceHelper.getInstance();
        for (Player p : prefs.getPlayers()) {
            if (p.name.equals(player.name)) return p.index;
        }
        return -1;
    }
}
