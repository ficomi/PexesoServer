/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

/**
 * V této tříde jsou uložené všechny příkazy,
 *  
 * @author Miloslav Fico
 */



import java.util.HashMap;
public class CommandMap {
    
    private final HashMap<String,ICommands> RECIVE_COMMANDS;

    public CommandMap() {
        RECIVE_COMMANDS = new HashMap<>();
        
    }
    
   public void AddCommandToMap(ICommands command){
   RECIVE_COMMANDS.put(command.getName(), command);
   }
   
 
   
   public boolean isCommand(String key){
   return RECIVE_COMMANDS.containsKey(key);
   }
   
   public ICommands getCommandClass(String key){
   return RECIVE_COMMANDS.get(key);
   }
   
  
    
    
    
}
