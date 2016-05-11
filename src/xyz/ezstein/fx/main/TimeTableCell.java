package xyz.ezstein.fx.main;

import java.util.concurrent.TimeUnit;

import javafx.scene.control.*;
import xyz.ezstein.backend.*;

public class TimeTableCell extends TableCell<SplitEvent,Number> {
	private Label timeLabel;
	public TimeTableCell(){
		super();
		timeLabel=new Label("");
		setGraphic(timeLabel);
	}
	
	@Override
	public void updateItem(Number timeN, boolean empty){
		super.updateItem(timeN, empty);
		if(empty||timeN==null){
			setGraphic(null);
		} else {
			long time = (long) timeN;
			long hours = TimeUnit.NANOSECONDS.toHours(time);
			long minutes = TimeUnit.NANOSECONDS.toMinutes(time)-TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(time));
			long seconds = TimeUnit.NANOSECONDS.toSeconds(time)-TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(time));
			long milliseconds = TimeUnit.NANOSECONDS.toMillis(time)-TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(time));
			String timeText = hours+":"+minutes+":"+seconds+":"+milliseconds;
			timeLabel.setText(timeText);
			
			setGraphic(timeLabel);
		}
	}
}
