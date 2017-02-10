/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.dataaccess;

import client.Postgres;
import hu.daq.wp.Coach;
import hu.daq.wp.Entity;
import hu.daq.wp.MatchProfile;
import hu.daq.wp.Player;
import hu.daq.wp.Referee;
import hu.daq.wp.Team;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONException;

/**
 *
 * @author DAQ
 */
public class DAO {

    Postgres db;
    HashMap<String, Entity> cache;
    ObservableList<Team> teams;

    public DAO(Postgres db) {
        this.db = db;
        this.cache = new HashMap<String, Entity>();
    }

    public Player getPlayer(Integer id) {
        Entity ent = this.getFromCache("Player::" + id.toString());
        if (ent != null) {
            return (Player) ent;
        }
        Player e = new Player();
        String sendstr = "select * from player where player_id=" + id.toString();
        HashMap<String, String> rec = null;

        try {
            rec = this.db.query(sendstr).get(0);

            e.id.set(Integer.parseInt(rec.get("player_id")));
            e.name.set(rec.get("name"));
            e.shortname.set(rec.get("shortname"));
            e.capnum.set(Integer.parseInt(rec.get("capnum")));
            e.team_id.set(Integer.parseInt(rec.get("team_id")));
            e.active.set(Boolean.parseBoolean(rec.get("active")));
            e.player_pic.set(Integer.parseInt(rec.get("player_pic")));
            this.putToCache(e);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        return e;
    }

    public void deletePlayer(Integer id) {
        try {
            this.getFromCache("Player::" + id.toString()).getDeleted().set(true);
        } catch (Exception e) {

        }
        this.removeFromCache("Player::" + id.toString());
        String sendstr = "delete from player where player_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
    }

    public void deletePlayer(Player player) {
        player.getDeleted().set(true);
        this.deletePlayer(player.getID());
    }

    public boolean save(Player e) {
        if (!e.isChanged()) {
            return true;
        }
        String sendstr;
        if (e.id.getValue().equals(0)) {
            sendstr = "insert into player (name,shortname,capnum,team_id,player_pic,active) values("
                    + "'" + e.name.getValueSafe() + "', "
                    + "'" + e.shortname.getValueSafe() + "', "
                    + e.capnum.getValue().toString() + ", "
                    + e.team_id.getValue().toString() + ", "
                    + e.player_pic.getValue().toString() + ", "
                    + "'" + e.active.getValue().toString() + "'"
                    + ") returning *";
        } else {
            sendstr = "update player set (name,shortname,capnum,team_id,player_pic,active) =("
                    + "'" + e.name.getValueSafe() + "', "
                    + "'" + e.shortname.getValueSafe() + "', "
                    + e.capnum.getValue().toString() + ", "
                    + e.team_id.getValue().toString() + ", "
                    + e.player_pic.getValue().toString() + ", "
                    + "'" + e.active.getValue().toString() + "'"
                    + ") where player_id=" + e.id.getValue().toString()
                    + " returning *";
        }
        try {
            Integer id = Integer.parseInt(db.query(sendstr).get(0).get("player_id"));
            if (!id.equals(e.id.get())) {
                e.id.set(id);
            }

        } catch (Exception ex) {
            System.out.println("Failed:" + ex.toString());
            return false;
        }
        this.putToCache(e);
        e.setToChanged(false);

        return true;
    }

    public Coach getCoach(Integer id) {
        Entity ent = this.getFromCache("Coach::" + id.toString());
        if (ent != null) {
            return (Coach) ent;
        }
        Coach e = new Coach();
        String sendstr = "select * from coach where coach_id=" + id.toString();
        HashMap<String, String> rec = null;

        try {
            rec = this.db.query(sendstr).get(0);
            e.id.set(Integer.parseInt(rec.get("coach_id")));
            e.name.set(rec.get("name"));
            e.team_id.set(Integer.parseInt(rec.get("team_id")));
            e.coach_pic.set(Integer.parseInt(rec.get("coach_pic")));
            this.putToCache(e);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        return e;
    }

    public boolean save(Coach e) {
        if (!e.isChanged()) {
            return true;
        }
        String sendstr;
        if (e.id.getValue().equals(0)) {
            sendstr = "insert into coach (name,team_id,coach_pic) values("
                    + "'" + e.name.getValueSafe() + "', "
                    + e.team_id.getValue().toString() + ", "
                    + e.coach_pic.getValue().toString()
                    + ") returning *";
        } else {
            sendstr = "update coach set (name,team_id,coach_pic) =("
                    + "'" + e.name.getValueSafe() + "', "
                    + e.team_id.getValue().toString() + ", "
                    + e.coach_pic.getValue().toString()
                    + ") where coach_id=" + e.id.getValue().toString()
                    + " returning *";
        }
        try {
            Integer id = Integer.parseInt(db.query(sendstr).get(0).get("coach_id"));
            if (!id.equals(e.id.get())) {
                e.id.set(id);
            }

        } catch (Exception ex) {
            System.out.println("Failed:" + ex.toString());
            return false;
        }
        this.putToCache(e);
        e.setToChanged(false);
        return true;
    }

    public void deleteCoach(Integer id) {
        try {
            this.getFromCache("Coach::" + id.toString()).getDeleted().set(true);
        } catch (Exception e) {

        }
        this.removeFromCache("Coach::" + id.toString());
        String sendstr = "delete from coach where coach_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
    }

    public void deleteCoach(Coach coach) {
        coach.getDeleted().set(true);
        this.deleteCoach(coach.getID());
    }

    public Referee getReferee(Integer id) {
        Entity ent = this.getFromCache("Referee::" + id.toString());
        if (ent != null) {
            return (Referee) ent;
        }
        Referee e = new Referee();
        String sendstr = "select * from referee where referee_id=" + id.toString();
        HashMap<String, String> rec = null;

        try {
            rec = this.db.query(sendstr).get(0);
            e.id.set(Integer.parseInt(rec.get("referee_id")));
            e.name.set(rec.get("name"));
            e.referee_pic.set(Integer.parseInt(rec.get("referee_pic")));
            this.putToCache(e);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        return e;
    }

    public boolean save(Referee e) {
        if (!e.isChanged()) {
            return true;
        }
        String sendstr;

        if (e.id.getValue().equals(0)) {
            sendstr = "insert into referee (name,referee_pic) values("
                    + "'" + e.name.getValueSafe() + "', "
                    + e.referee_pic.getValue().toString()
                    + ") returning *";
        } else {
            sendstr = "update referee set (name,referee_pic) =("
                    + "'" + e.name.getValueSafe() + "', "
                    + e.referee_pic.getValue().toString()
                    + ") where referee_id=" + e.id.getValue().toString()
                    + " returning *";
        }
        try {
            Integer id = Integer.parseInt(db.query(sendstr).get(0).get("referee_id"));
            if (!id.equals(e.id.get())) {
                e.id.set(id);
            }

        } catch (Exception ex) {
            System.out.println("Failed:" + ex.toString());
            return false;
        }
        this.putToCache(e);
        e.setToChanged(false);
        return true;
    }

    public void deleteReferee(Integer id) {
        try {
            this.getFromCache("Referee::" + id.toString()).getDeleted().set(true);
        } catch (Exception e) {

        }
        this.removeFromCache("Referee::" + id.toString());
        String sendstr = "delete from referee where referee_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
    }

    public void deleteReferee(Referee referee) {
        referee.getDeleted().set(true);
        this.deleteReferee(referee.getID());
    }

    public MatchProfile getMatchProfile(Integer id) {
        Entity ent = this.getFromCache("MatchProfile::" + id.toString());
        if (ent != null) {
            return (MatchProfile) ent;
        }
        MatchProfile e = new MatchProfile();
        String sendstr = "select * from matchprofile where matchprofile_id=" + id.toString();
        HashMap<String, String> rec = null;

        try {
            rec = this.db.query(sendstr).get(0);
            e.id.set(Integer.parseInt(rec.get("matchprofile_id")));
            e.profilename.set(rec.get("profilename"));
            e.profile.set(rec.get("profile"));
            e.setProfile(rec.get("profile"));
            this.putToCache(e);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        return e;
    }

    public boolean save(MatchProfile e) {
        if (!e.isChanged()) {
            return true;
        }
        String sendstr;
        if (e.id.getValue().equals(0)) {
            try {
                sendstr = "insert into matchprofile (profilename,profile) values("
                        + "'" + e.profilename.getValueSafe() + "', "
                        + "'" + e.getProfile() + "'"
                        + ") returning *";
            } catch (JSONException ex) {
                System.out.println("Failed:" + ex.toString());
                return false;
            }
        } else {
            try {
                sendstr = "update matchprofile set (profilename,profile) =("
                        + "'" + e.profilename.getValueSafe() + "', "
                        + "'" + e.getProfile() + "'"
                        + ") where matchprofile_id=" + e.id.getValue().toString()
                        + " returning *";
            } catch (JSONException ex) {
                System.out.println("Failed:" + ex.toString());
                return false;
            }
        }
        try {
            Integer id = Integer.parseInt(db.query(sendstr).get(0).get("matchprofile_id"));
            if (!id.equals(e.id.get())) {
                e.id.set(id);
            }

        } catch (Exception ex) {
            System.out.println("Failed:" + ex.toString());
            return false;
        }
        this.putToCache(e);
        e.setToChanged(false);
        return true;
    }

    public void deleteMatchProfile(Integer id) {
        try {
            this.getFromCache("MatchProfile::" + id.toString()).getDeleted().set(true);
        } catch (Exception e) {

        }
        this.removeFromCache("MatchProfile::" + id.toString());
        String sendstr = "delete from matchprofile where matchprofile_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
    }

    public void deleteMatchProfile(MatchProfile e) {
        e.getDeleted().set(true);
        this.deleteMatchProfile(e.getID());
    }

    public Team getTeam(Integer id) {
        Entity ent = this.getFromCache("Team::" + id.toString());
        if (ent != null) {
            return (Team) ent;
        }
        Team e = new Team();
        String sendstr = "select * from team where team_id=" + id.toString();
        HashMap<String, String> rec = null;

        try {
            rec = this.db.query(sendstr).get(0);
            e.id.set(Integer.parseInt(rec.get("team_id")));
            e.teamname.set(rec.get("teamname"));
            this.putToCache(e);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        return e;
    }

    public boolean save(Team e) {
        if (!e.isChanged()) {
            return true;
        }
        String sendstr;

        if (e.id.getValue().equals(0)) {
            sendstr = "insert into team (teamname) values("
                    + "'" + e.teamname.getValueSafe() + "') returning *";
        } else {
            sendstr = "update team set (teamname) =("
                    + "'" + e.teamname.getValueSafe() + "') where team_id=" + e.id.getValue().toString()
                    + " returning *";
        }
        try {
            Integer id = Integer.parseInt(db.query(sendstr).get(0).get("referee_id"));
            if (!id.equals(e.id.get())) {
                e.id.set(id);
            }

        } catch (Exception ex) {
            System.out.println("Failed:" + ex.toString());
            return false;
        }
        this.putToCache(e);
        e.setToChanged(false);
        return true;
    }

    public void deleteTeam(Integer id) {
        try {
            this.getFromCache("Team::" + id.toString()).getDeleted().set(true);
        } catch (Exception e) {

        }
        this.removeFromCache("Team::" + id.toString());
        String sendstr = "delete from team where team_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
    }

    public void deleteTeam(Team team) {
        team.getDeleted().set(true);
        this.deletePlayer(team.getID());
    }

    public void deleteTeamWithPlayers(Integer id) {
        this.removeFromCache("Team::" + id.toString());
        String sendstr = "delete from team where team_id=" + id.toString();
        try {
            this.db.execquery(sendstr);
        } catch (Exception ex) {
            // throw new Exception("Load Player "+id.toString()+" failed");
        }
        this.getPlayersOfTeam(id).stream().forEach(E -> this.deletePlayer(E));
    }

    public void deleteTeamWithPlayers(Team team) {
        this.deleteTeamWithPlayers(team.getID());
    }

    private ArrayList<Player> getPlayersLowLevel(String sendstr) {
        ArrayList<Player> playerlist = new ArrayList<Player>();
        List<HashMap<String, String>> rec;

        try {
            rec = this.db.query(sendstr);
            playerlist = rec.parallelStream().map(E -> {
                Player p = this.getPlayer(Integer.parseInt(E.get("player_id")));
                return p;
            }).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {

        }
        return playerlist;
    }

    public ArrayList<Player> getPlayersOfTeam(Integer id) {
        String sendstr = "select player_id from player where team_id=" + id.toString();
        return this.getPlayersLowLevel(sendstr);
    }

    public ArrayList<Player> getActivePlayersOfTeam(Integer id) {
        String sendstr = "select * from player where team_id=" + id.toString() + " and active='t' order by capnum";
        return this.getPlayersLowLevel(sendstr);
    }

    public ArrayList<Player> getPassivePlayersOfTeam(Integer id) {
        String sendstr = "select * from player where team_id=" + id.toString() + " and active='f'";
        return this.getPlayersLowLevel(sendstr);
    }

    public Coach getCoachOfTeam(Integer id) {
        String sendstr = "select * from coach where team_id=" + id.toString();

        List<HashMap<String, String>> rec;
        try {
            rec = this.db.query(sendstr);
            if (rec.size() > 0) {
                Coach c = this.getCoach(Integer.parseInt(rec.get(0).get("coach_id")));
                return c;
            }

        } catch (Exception e) {

        }
        return null;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> relist = new ArrayList<Player>();
        String sendstr = "select * from player order by name";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                Player t = this.getPlayer(Integer.parseInt(E.get("player_id")));
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("baj" + e.toString());
        }
        return relist;
    }

    public ArrayList<Coach> getCoaches() {
        ArrayList<Coach> relist = new ArrayList<Coach>();
        String sendstr = "select * from coach order by name";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                Coach t = this.getCoach(Integer.parseInt(E.get("coach_id")));
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("baj" + e.toString());
        }
        return relist;
    }

    public ArrayList<Referee> getReferees() {
        ArrayList<Referee> relist = new ArrayList<Referee>();
        String sendstr = "select * from referee order by name";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                Referee t = this.getReferee(Integer.parseInt(E.get("referee_id")));
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("baj" + e.toString());
        }
        return relist;
    }

    public ObservableList<Team> getTeams() {
        if (this.teams == null){
            this.teams = FXCollections.observableArrayList(new ArrayList<Team>());
        }
        //ArrayList<Team> relist = ;
        String sendstr = "select * from team order by teamname";
        try {
            this.teams.setAll(db.query(sendstr).stream().map(E -> {
                Team t = this.getTeam(Integer.parseInt(E.get("team_id")));
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("baj" + e.toString());
        }
        return this.teams;
    }

    public ArrayList<MatchProfile> getMatchProfiles() {
        ArrayList<MatchProfile> relist = new ArrayList<MatchProfile>();
        String sendstr = "select * from matchprofile order by profilename";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                MatchProfile t = this.getMatchProfile(Integer.parseInt(E.get("matchprofile_id")));
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("baj" + e.toString());
        }
        return relist;
    }

    private Entity getFromCache(String cacheid) {
        return this.cache.get(cacheid);
    }

    private void putToCache(Entity ent) {
        this.cache.putIfAbsent(ent.toInternalString(), ent);
    }

    private void removeFromCache(String cacheid) {
        this.cache.remove(cacheid);
    }
}
