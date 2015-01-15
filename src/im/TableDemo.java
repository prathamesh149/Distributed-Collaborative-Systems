package im;

/*
 * TableDemo.java requires no other files.
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;

/**
 * TableDemo is just like SimpleTableDemo, except that it uses a custom
 * TableModel.
 */
public class TableDemo extends JPanel {

	MyTableModel myTableModel;
	//Map<String,String> map;
	
	public TableDemo() {
		super(new GridLayout(1, 0));

		myTableModel = new MyTableModel();
		JTable table = new JTable(myTableModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
	}

	class MyTableModel extends AbstractTableModel {
		private String[] columnNames = { "Name", "Status" };
		private Object[][] data = { { "Alice", "X" }, { "Bob", "Y" },
				{ "Cathy", "Z" } };

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {

			data[row][col] = value;
			fireTableCellUpdated(row, col);

		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return true;
		}

	}

	public void launchAndShowTable() {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
				
			}
		};
		SwingUtilities.invokeLater(r);
	}
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	public void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("TableDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		TableDemo newContentPane = new TableDemo();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public void runner(Map<String,String> map) {
		Runnable t = new Runnable() {
			
			@Override
			public void run() {
				for (Map.Entry<String, String> entry: map.entrySet()) {
					if ( entry.getKey().equalsIgnoreCase("Alice")) {
						myTableModel.setValueAt(entry.getValue(), 0, 1);
					} else if ( entry.getKey().equalsIgnoreCase("Bob")) {
						myTableModel.setValueAt(entry.getValue(), 1, 1);
					} else if ( entry.getKey().equalsIgnoreCase("Cathy")) {
						myTableModel.setValueAt(entry.getValue(), 2, 1);
					}
				}
			}
		};
	}
	
	
	public void updateGUI(Map<String,String> map) {
		runner(map);
		/*for (Map.Entry<String, String> entry: map.entrySet()) {
			if ( entry.getKey().equalsIgnoreCase("Alice")) {
				myTableModel.setValueAt(entry.getValue(), 0, 1);
			} else if ( entry.getKey().equalsIgnoreCase("Bob")) {
				myTableModel.setValueAt(entry.getValue(), 1, 1);
			} else if ( entry.getKey().equalsIgnoreCase("Cathy")) {
				myTableModel.setValueAt(entry.getValue(), 2, 1);
			}
		}*/
	}
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TableDemo td = new TableDemo();
				td.createAndShowGUI();
				System.out.println(td.myTableModel.getValueAt(1, 1));
			}
		});
	}
}