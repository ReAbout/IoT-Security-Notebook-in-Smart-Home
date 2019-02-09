/*Copyright (c) 2015-2050, JD Smart All rights reserved.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. */

package core.common;

import java.util.logging.*;

/** -- Level -- 
 *    SEVERE (highest value)
 *    WARNING
 *    INFO
 *    CONFIG
 *    FINE
 *    FINER
 *    FINEST 
 */
public class SingLog{  
    private volatile static SingLog singLog;  
    private static Logger log = null;

    private SingLog(){
        if(log == null){
            log = Logger.getLogger("lavasoft"); 
            log.setLevel(Level.ALL); 
        }
    }   

    /**
     * brief: 
     *
     * @Returns: 
     */
    public static SingLog getSingLog() {  
        if (singLog == null) {  
            synchronized (SingLog.class) {  
                if (singLog == null) {  
                    singLog = new SingLog();  
                }  
            }  
        }  
        return singLog;  
    }  

    public static Logger log() { 
        getSingLog();
        return log;
    } 

    public static Logger getLogger(){ 
        getSingLog();
        return log;
    } 
    /**
     * brief: 
     *
     * @Param: msg
     *
     * @Returns: 
     */
    public void debug(String msg) { 
        //log.logrb(Level.INFO, null, null, null, msg);
        System.out.print(msg + "\n");
    } 

    /**
     * brief: 
     *
     * @Param: msg
     *
     * @Returns: 
     */
    public void warning(String msg) { 
        //log.logrb(Level.WARNING, null, null, null, msg);
    } 

    /**
     * brief: 
     *
     * @Param: msg
     *
     * @Returns: 
     */
    public void error(String msg) { 
        log.logrb(Level.SEVERE, null, null, null, msg);
    } 
}  
