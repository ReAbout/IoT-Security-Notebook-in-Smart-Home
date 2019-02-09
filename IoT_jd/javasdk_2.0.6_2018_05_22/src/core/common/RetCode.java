package core.common;
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



public enum RetCode implements RetCodeInterface{
    OK(E_OK, 									"com.joylink.ok"),
    ERROR_PARAM_INVAID(E_ERROR_PARAM_INVAID,    "com.joylink.error.invalidParam"),
    ERROR(E_ERROR,                          	"com.joylink.error");

    //-----------------------------------------------------------------------------------------------------------
    private int iVal=0;
    private String sVal;

    /**
     * brief:Must hava this costruct fun
     *       Must private 
     *
     * @Param: value
     * @Param: str
     *
     * @Returns: 
     */
    private RetCode(int value, String str){
        iVal = value;
        this.sVal = str;
    }

    /**
     * brief: 
     *
     * @Returns: 
     */
    public int getIVal(){
        return iVal;
    }  

    /**
     * brief: 
     *
     * @Returns: 
     */
    public String getSVal(){
        return sVal;
    }  

    /**
     * brief: 
     *
     * @Returns: 
     */
    public static void test(){  
        System.out.println(">>>>>:" + RetCode.OK.getIVal());
        System.out.println(">>>>>:" + RetCode.OK.getSVal());
    } 
}
