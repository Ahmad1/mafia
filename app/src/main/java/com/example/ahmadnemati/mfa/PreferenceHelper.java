package com.example.ahmadnemati.mfa;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.ahmadnemati.mfa.domain.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PreferenceHelper {

    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    public static final String MEMBER_USERNAME = "member_username";
    public static final String MEMBER_PASSWORD = "member_password";

    public static final String ACCESS_CODE = "code";
    public static final String NUMBER_OF_PLAYERS = "number";
    public static final String PLAYER_NAMES = "names";

    public static final String LAST_APP_VERSION = "last_app_version";
    public static final String USER_OPTIN_TO_RECEIVE_NOTIFICATION = "USER_OPTIN_TO_RECEIVE_NOTIFICATION";
    public static final String USER_OPTIN_TO_RECEIVE_NOTIFICATION_FOR_RK = "USER_OPTIN_TO_RECEIVE_NOTIFICATION_FOR_RK";

    public static final String DEVICE_ID = "device_id"; // TelephonyManager.getDeviceId

    public static final String LAST_NOTICE_SHOWN_TIMESTAMP = "LAST_NOTICE_SHOWN_TIMESTAMP";
    public static final String NOTICE_ALERT_MESSAGE_IDS = "NOTICE_ALERT_MESSAGE_IDS";
    public static final String APP_LAUNCH_SOURCE = "launch_from";

    private static PreferenceHelper mInstance;

    private PreferenceHelper(SharedPreferences settings) {
        mSettings = settings;
        editor = settings.edit();
    }

    public static PreferenceHelper getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Did you call PreferenceHelper.initialize()?");
        }
        return mInstance;
    }

    public static void initialize(SharedPreferences settings) {
        if (mInstance == null) {
            mInstance = new PreferenceHelper(settings);
        } else {
            throw new IllegalStateException("You already called PreferenceHelper.initialize()!");
        }
    }

    @Nullable
    public String getDeviceId() {
        String deviceId = mSettings.getString(DEVICE_ID, null);
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString();
            editor.putString(DEVICE_ID, deviceId);
            editor.commit();
        }

        return deviceId;
    }

    public void setAccessCode(String code) {
        editor.putString(ACCESS_CODE, code);
        editor.commit();
    }

    @Nullable
    public String getAccessCode() {
        return mSettings.getString(ACCESS_CODE, "000");
    }


    public void setCurrentNumberOfPpl(String number) {
        editor.putString(NUMBER_OF_PLAYERS, number);
        editor.commit();
    }

    @Nullable
    public String getCurrentNumberOfPpl() {
        return mSettings.getString(NUMBER_OF_PLAYERS, "0");
    }


    public void setPeopleNames(String names) {
        editor.putString(PLAYER_NAMES, names);
        editor.commit();
    }

    @Nullable
    public String getPeopleNames() {
        return mSettings.getString(PLAYER_NAMES, "");
    }

    public void savePerson(Player player, int index) {
        editor.putString("p_name" + index, player.name);
        editor.putInt("p_index" + index, player.index);
        editor.putBoolean("p_is_alive" + index, player.isAlive);
        editor.putBoolean("p_is_angel" + index, player.isAngel);
        editor.putInt("p_life" + index, player.lifeWishes);
        editor.putInt("p_death" + index, player.deathWishes);
        editor.putInt("p_role" + index, player.convertRoleToInt(player.role));
        editor.commit();
    }

    private Player getPerson(int index) {
        String name = mSettings.getString("p_name" + index, null);
        if (name == null) return null;
        Player player = new Player();
        player.name = name;
        player.index = mSettings.getInt("p_index" + index, 0);
        player.isAlive = mSettings.getBoolean("p_is_alive" + index, true);
        player.isAngel = mSettings.getBoolean("p_is_angel" + index, true);
        player.lifeWishes = mSettings.getInt("p_life" + index, 0);
        player.deathWishes = mSettings.getInt("p_death" + index, 0);
        player.role = player.convertIntToRole(mSettings.getInt("p_role" + index, 1));
        return player;
    }

    public ArrayList<Player> getPlayers() {
        int pplCount = Integer.valueOf(getCurrentNumberOfPpl());
        if (pplCount < 1) return null;
        Player p = getPerson(0);
        if (p.name == null) return null;
        ArrayList<Player> people = new ArrayList<>();
        for (int i = 0; i < pplCount; i++) {
            people.add(getPerson(i));
        }
        return people;
    }


    // Base on getCurrentnumberOfPpl fill out a list of person and send to who ever wants it.



/*public void saveMember(HLMember member) {
        editor.putString("member_id", member.memberId);
        editor.putString("first_name", member.firstName);
        editor.putString("last_name", member.lastName);
        editor.putString("email", member.email);
        editor.putString("zipcode", member.postalCode);
        editor.putString("country_iso", member.country);
        editor.putString("gender", member.gender);
        editor.commit();
    }

    public HLMember getMember() {
        HLMember member = new HLMember();
        member.memberId = mSettings.getString("member_id", null);
        member.firstName = mSettings.getString("first_name", null);
        member.lastName = mSettings.getString("last_name", null);
        member.email = mSettings.getString("email", null);
        member.postalCode = mSettings.getString("zipcode", null);
        member.country = mSettings.getString("country_iso", "US");
        member.gender = mSettings.getString("gender", "F");

        if (member.memberId != null) {
            return member;
        }

        return null;
    }

    public void clearMember() {
        editor.remove("member_id");
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("email");
        editor.remove("zipcode");
        editor.remove("country_iso");
        editor.remove("gender");
        editor.remove(MEMBER_USERNAME);
        editor.remove(MEMBER_PASSWORD);
        editor.commit();
    }
*/


    public void saveLastNoticeShownTime(long timestamp) {
        editor.putLong(LAST_NOTICE_SHOWN_TIMESTAMP, timestamp);
        editor.commit();
    }

    public long getLastNoticeShownTime() {
        return mSettings.getLong(LAST_NOTICE_SHOWN_TIMESTAMP, 0);
    }

    public boolean getMemberWantToReceiveHLNotification() {
        return mSettings.getBoolean(USER_OPTIN_TO_RECEIVE_NOTIFICATION, true);
    }

    public void saveNoticeAlertMessageIds(String jsonString) {
        editor.putString(NOTICE_ALERT_MESSAGE_IDS, jsonString);
        editor.commit();
    }

    public String getNoticeAlertMessageIds() {
        return mSettings.getString(NOTICE_ALERT_MESSAGE_IDS, "");
    }


    public void saveMemberWantToReceiveRKNotification(boolean flag) {
        editor.putBoolean(USER_OPTIN_TO_RECEIVE_NOTIFICATION_FOR_RK, flag);
        editor.commit();
    }

    public void saveMemberWantToReceiveHLNotification(boolean flag) {
        editor.putBoolean(USER_OPTIN_TO_RECEIVE_NOTIFICATION, flag);
        editor.commit();

    }

    public boolean getMemberWantToReceiveRKNotification() {
        return mSettings.getBoolean(USER_OPTIN_TO_RECEIVE_NOTIFICATION_FOR_RK, true);
    }


    public void saveLastAppVersion(int appVersion) {
        editor.putInt(LAST_APP_VERSION, appVersion);
        editor.commit();
    }

    public int getLastAppVersion() {
        return mSettings.getInt(LAST_APP_VERSION, Integer.MIN_VALUE);
    }

    public void saveMemberUsernamePassword(String username, String password) {
        editor.putString(MEMBER_USERNAME, username);
        editor.putString(MEMBER_PASSWORD, password);
        editor.commit();
    }


    public void clearMemberUsernamePassword() {
        editor.remove(MEMBER_USERNAME);
        editor.remove(MEMBER_PASSWORD);
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }


    public void saveMemberUsername(String username) {
        editor.putString(MEMBER_USERNAME, username);
        editor.commit();
    }

    public String getMemberUsername() {
        return mSettings.getString(MEMBER_USERNAME, null);
    }

    public void saveMemberPassword(String password) {
        editor.putString(MEMBER_PASSWORD, password);
        editor.commit();
    }

    public String getMemberPassword() {
        return mSettings.getString(MEMBER_PASSWORD, null);
    }

}