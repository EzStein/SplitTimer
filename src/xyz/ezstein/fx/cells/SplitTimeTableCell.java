package xyz.ezstein.fx.cells;

import javafx.scene.control.*;
import xyz.ezstein.backend.*;
import xyz.ezstein.backend.util.*;
import xyz.ezstein.fx.observable.*;

public class SplitTimeTableCell extends TableCell<SplitEvent, SplitTime> {
	private Label timeLabel;
	public SplitTimeTableCell(){
		super();
		timeLabel=new Label("");
		setGraphic(timeLabel);
	}
	
	@Override
	public void updateItem(SplitTime splitTime, boolean empty){
		super.updateItem(splitTime, empty);
		if(empty||splitTime==null){
			setGraphic(null);
		} else {
			long time = splitTime.getSplitTime();
			SplitTimeType type = splitTime.getType();
			timeLabel.setText(Util.nanosToReadable(time));
			String style = "";
			switch(type){
				case LN: style="-fx-text-fill: #00FF66";
				break;
				case GN: style="-fx-text-fill: blue";
				break;
				case LP: style="-fx-text-fill: orange";
				break;
				case GP: style="-fx-text-fill: red";
				break;
				case NEUTRAL: style="-fx-text-fill: black";
				break;
				case GOLD: style="-fx-text-fill: #fbfd0a";
				break;
			}
			
			timeLabel.setStyle(style);
			setGraphic(timeLabel);
		}
	}
}
