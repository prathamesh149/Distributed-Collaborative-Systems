package echo.modular;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnawareTextFieldListener implements ActionListener {
		
		private MainPanel mainpanel;
		
		public UnawareTextFieldListener(MainPanel mainpanel) {
			this.mainpanel = mainpanel;
		}
		
		public void actionPerformed(ActionEvent e) {
			String message_line = e.getActionCommand();
			
			mainpanel.addHistoryText(message_line);
			mainpanel.setUnawareText("");
		}
}