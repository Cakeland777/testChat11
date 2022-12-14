package server1;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Data;

enum CommandType {
	NORMAL_CMD,
	EXIT_CMD
}

@Data
class Message {
	private CommandType commandType;
	private String message;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private Date createDate;
	private String roomName;
	
	private Message(CommandType commandType, String message, String roomName) {
		this.setCommandType(commandType);
		this.message = message;
		this.createDate = new Date(Calendar.getInstance().getTime().getTime());
		this.roomName=roomName;
	}
	
	static Message of(String message,String roomName) {
		return new Message(CommandType.NORMAL_CMD, message, roomName);
	}
	
	static Message exitMessage() {
		return new Message(CommandType.EXIT_CMD, "", "");
	}
	
	public String getMessageStr() {
		return "[" + sdf.format(createDate) + "] : " + message;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public String getMessage() {
		return message;
	}
	public String getRoomName() {
		return roomName;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}
}

//Consumer ( μλΉμ )
public class Logger implements Runnable {
  private LinkedBlockingQueue<Message> queue;
  private PrintStream out;
  private boolean dbWrite = false;
  private Connection conn = null;
  private PreparedStatement pstmt = null;
  
  public Logger() {
      try {
    	  this.queue = new LinkedBlockingQueue<Message>();
    	  this.out = new PrintStream(new BufferedOutputStream(new FileOutputStream(Env.getLoggerFileName())));
          this.dbWrite = Boolean.parseBoolean(Env.getProperty("logger.DBWrite", "false"));
          
          open();
          pstmt = conn.prepareStatement(Env.getProperty("INSERT_LOG"));
      } catch (Exception e) {
    	  e.printStackTrace();
      }

      new Thread(this).start();
  }
  
  @Override
  public void run() {
      try {
          while(!Thread.currentThread().isInterrupted()) {
        	  Message msg = queue.take();
        	  if (msg.getCommandType() == CommandType.EXIT_CMD) {
        		  break;
        	  }
        	  out.println(msg.getMessageStr());
        	  if (this.dbWrite) {
	        	  //DBμ κΈ°λ‘
	        	  writeLogDB(msg);
        	  }
        	  try {
        		  Thread.sleep(10000);
        	  }catch (Exception ex) {
        		  
        	  }
        	  
          }
          
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
    	  out.close();
    	  close();
      }
  }
  
  public void write(String msg,String roomName) {
	  queue.offer(Message.of(msg,roomName));
  }
  
  public void endLogger() {
	queue.offer(Message.exitMessage());
  }
  

	private void open() {
		try {
			Class.forName(Env.getProperty("driverClass"));
			System.out.println("JDBC λλΌμ΄λ² λ‘λ© μ±κ³΅");
			
			conn = DriverManager.getConnection(Env.getProperty("dbServerConn")
					, Env.getProperty("dbUser")
					, Env.getProperty("dbPasswd"));
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void close() {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeLogDB(Message message) {
		try {
			//λ‘κ·Έ μ λ³΄ μ€μ 
			pstmt.setDate(1, message.getCreateDate());
			pstmt.setString(2, message.getMessage());
			pstmt.setString(3, message.getRoomName());
			pstmt.executeUpdate();
			conn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	  
	}
}
