/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Postgres;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class Team extends Entity{
    
    protected SimpleIntegerProperty team_id = new SimpleIntegerProperty();
    protected SimpleStringProperty teamname = new SimpleStringProperty(); 
    protected SimpleBooleanProperty playernumok = new SimpleBooleanProperty(true);
    protected SimpleBooleanProperty capnumok = new SimpleBooleanProperty(true);    
    protected SimpleBooleanProperty visible;
    
    public Team(Postgres db){
        super(db);
        this.team_id.addListener(changelistener);
        this.teamname.addListener(changelistener);
        this.subscribe();
        
    }     
    
    public static Callback<Team, Observable[]> extractor(){
        return (Team t) -> new Observable[]{t.getTeamname()};
    }
    
    @Override
    public boolean load(Integer pk) {
        String sendstr = "select *,"
                + " (select count(*) from player where team_id=team.team_id and active='t')=14 as playernumok,"
                + " (select count(distinct capnum) from player where team_id=team.team_id and active='t')=14 as capnumok"
                + " from team where team_id="+pk.toString();
        boolean success = true;
        HashMap<String,String> rec = null;
        try{
            rec = this.db.query(sendstr).get(0);
        } catch (Exception e){
            success = false;
        }
        return this.load(rec);        
    }

    @Override
    public boolean load(HashMap<String, String> data) {
        try{
            System.out.println(data);
            this.team_id.set(Integer.parseInt(data.get("team_id")));
            this.teamname.set(data.get("teamname"));
            this.playernumok.set(Boolean.parseBoolean(data.get("playernumok")));
            System.out.println("p:"+Boolean.parseBoolean(data.get("playernumok")));            
            this.capnumok.set(Boolean.parseBoolean(data.get("capnumok")));    
            System.out.println("c:"+Boolean.parseBoolean(data.get("capnumok")));            
        } catch (Exception ex){
            return false;
        }
        this.changed.set(false);
        return true;
    }
    
    @Override
    public boolean save() {
        String sendstr;
        if (this.team_id.getValue().equals(0)){
            sendstr = "insert into team (teamname) values("
                    +"'"+this.teamname.getValueSafe()+"') returning *";
        } else {
            sendstr = "update team set (teamname) =("
                    +"'"+this.teamname.getValueSafe()+"') where team_id="+this.team_id.getValue().toString()
                    +" returning *";        
        }
        try{
            this.load(db.query(sendstr).get(0));
        } catch (Exception ex){
          return false;          
        }
        this.changed.set(false);
        return true;        
    }
    
    public void reset(){
        this.team_id.set(0);
        this.teamname.set("");
        this.changed.set(false);
    }
    
    public SimpleIntegerProperty getTeam_id() {
        return team_id;
    }

    public SimpleStringProperty getTeamname() {
        return teamname;
    }

    public SimpleBooleanProperty getPlayernumok() {
        return playernumok;
    }

    public SimpleBooleanProperty getCapnumok() {
        return capnumok;
    }
    
    @Override
    public String toString(){
        return this.teamname.getValueSafe();
    }
    
    private ArrayList<Player> getPlayersLowLevel(String sendstr){
         ArrayList<Player> playerlist = new ArrayList<Player>();
        List<HashMap<String,String>> rec;
        
        try{
            rec = this.db.query(sendstr);
            playerlist = rec.parallelStream().map(new Function<HashMap<String,String>, Player>(){
                @Override
                public Player apply(HashMap hm){
                    Player p = new Player(db);
                    p.load(hm);
                    return p;
                
                }
            }).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e){

        }
        return playerlist;    
    }

    public ArrayList<Player> getPlayers(){
        String sendstr = "select * from player where team_id="+this.team_id.getValue().toString();
        return this.getPlayersLowLevel(sendstr);
    }
    
    public ArrayList<Player> getActivePlayers(){
        String sendstr = "select * from player where team_id="+this.team_id.getValue().toString()+" and active='t'";
        return this.getPlayersLowLevel(sendstr);
    }    
    
    public ArrayList<Player> getPassivePlayers(){
        String sendstr = "select * from player where team_id="+this.team_id.getValue().toString()+" and active='f'";
        return this.getPlayersLowLevel(sendstr);
    }    
    
    public Coach getCoach(){
        String sendstr = "select * from coach where team_id="+this.team_id.getValue().toString();
        
        List<HashMap<String,String>> rec;
        try{
            rec = this.db.query(sendstr);
            if (rec.size()>0){
                Coach c = new Coach(this.db);
                c.load(rec.get(0));
                return c;
            }
  
        } catch (Exception e){

        }
        return null; 
    }
    
    
    @Override
    public void subscribe() {
        this.db.subscribe("team", this);
    }

    @Override
    public void unsubscribe() {
        this.db.unsubscribe("team", this);
    }

    @Override
    public void finalize() throws Throwable {
        this.db.unsubscribe("team", this);
        
    }

    @Override
    public void onNotify() {
        this.load(this.team_id.getValue());
    }
    
    //Returns the id of the object in a notification mechanism compatible form
    @Override
    public Integer getID() {
        return this.team_id.getValue();
    }    

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible.set(visible);
    }
    
    
}
