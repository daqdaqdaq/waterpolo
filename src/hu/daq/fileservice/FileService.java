/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.fileservice;

import client.Postgres;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;



/**
 *
 * @author DAQ
 */
public class FileService {
  
    Postgres db;
    private HashMap<Integer, byte[]> filecahce = new HashMap<Integer, byte[]>();
    

    private static final FileService instance = new FileService();

    private FileService(){  } 
    
    public static FileService getInst(){
        return instance;
    }
    
    public FileService setDb(Postgres db){
        this.db = db;
        return this;
    }
    
    public ByteArrayInputStream getFile(Integer id) throws FileNotFoundException {
        //Search the file in the cache
        byte[] buf = this.filecahce.get(id);
        if (buf==null){
            //file not founf in the cache search for it in the db
            String sendstr = "select binarydata from file where file_id="+id.toString();
            try{
                buf = Base64.decode(db.query(sendstr).get(0).get("binarydata"));
                this.filecahce.put(id, buf);
            } catch (Exception ex){
                throw new FileNotFoundException();
            }
        }
        return new ByteArrayInputStream(buf);
    }
    
    public Integer saveFile(File file) throws IOException{
        return this.saveFile(IOUtils.toByteArray(new FileInputStream(file)));
    }

    public Integer saveFile(InputStream is) throws IOException{
        return this.saveFile(IOUtils.toByteArray(is));
    }    
    
    public Integer saveFile(byte[] fis) throws IOException{
        try{
        
            String sendstr = "insert into file (binarydata) values('"+Base64.encode(fis)+"') returning file_id";
            Integer fid = Integer.parseInt(this.db.query(sendstr).get(0).get("file_id"));
            this.filecahce.put(fid, fis);
            return fid;
        } catch (NumberFormatException ex){
            throw new IOException("Storing file has failed.");
        }    
    }

}
