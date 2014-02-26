/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.ingest;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.sleuthkit.autopsy.ingest.IngestMessage.*;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.autopsy.coreutils.MessageNotifyUtil;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.Logger;

/**
 * Notification window showing messages from modules to user
 * 
 */
class IngestMessagePanel extends JPanel implements TableModelListener {

    private MessageTableModel tableModel;
    private MessageTableRenderer renderer;
    private IngestMessageMainPanel mainPanel;
    private static Font visitedFont = new Font("Arial", Font.PLAIN, 12);
    private static Font notVisitedFont = new Font("Arial", Font.BOLD, 12);
    private static Color ERROR_COLOR = new Color(255, 90, 90);
    private volatile int lastRowSelected = -1;
    private volatile long totalMessages = 0;
    private static final Logger logger = Logger.getLogger(IngestMessagePanel.class.getName());
    private static PropertyChangeSupport messagePcs = new PropertyChangeSupport(IngestMessagePanel.class);
    static final String TOTAL_NUM_MESSAGES_CHANGED = "TOTAL_NUM_MESSAGES_CHANGED"; // total number of messages changed
    static final String MESSAGES_BOX_CLEARED = "MESSAGES_BOX_CLEARED"; // all messaged in inbox were cleared
    static final String TOTAL_NUM_NEW_MESSAGES_CHANGED = "TOTAL_NUM_NEW_MESSAGES_CHANGED"; // total number of new messages changed

    /** Creates new form IngestMessagePanel */
    public IngestMessagePanel(IngestMessageMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        tableModel = new MessageTableModel();
        initComponents();
        customizeComponents();
    }
    
    public void markAllSeen() {
        tableModel.markAllSeen();
    }

    int getLastRowSelected() {
        return this.lastRowSelected;
    }

    synchronized IngestMessageGroup getSelectedMessage() {
        if (lastRowSelected < 0) {
            return null;
        }

        return tableModel.getMessageGroup(lastRowSelected);
    }

    synchronized IngestMessageGroup getMessageGroup(int rowNumber) {
        return tableModel.getMessageGroup(rowNumber);
    }

    synchronized static void addPropertyChangeSupportListener(PropertyChangeListener l) {
        messagePcs.addPropertyChangeListener(l);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        messageTable = new javax.swing.JTable();
        controlPanel = new javax.swing.JPanel();
        sortByLabel = new javax.swing.JLabel();
        sortByComboBox = new javax.swing.JComboBox<String>();
        totalMessagesNameLabel = new javax.swing.JLabel();
        totalMessagesNameVal = new javax.swing.JLabel();
        totalUniqueMessagesNameLabel = new javax.swing.JLabel();
        totalUniqueMessagesNameVal = new javax.swing.JLabel();

        setOpaque(false);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setOpaque(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(32767, 32767));

        messageTable.setBackground(new java.awt.Color(221, 221, 235));
        messageTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        messageTable.setModel(tableModel);
        messageTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        messageTable.setAutoscrolls(false);
        messageTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        messageTable.setGridColor(new java.awt.Color(204, 204, 204));
        messageTable.setOpaque(false);
        messageTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        messageTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        messageTable.setShowHorizontalLines(false);
        messageTable.setShowVerticalLines(false);
        messageTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(messageTable);

        sortByLabel.setText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.sortByLabel.text")); // NOI18N

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Time", "Priority" }));
        sortByComboBox.setToolTipText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.sortByComboBox.toolTipText")); // NOI18N
        sortByComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByComboBoxActionPerformed(evt);
            }
        });

        totalMessagesNameLabel.setText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.totalMessagesNameLabel.text")); // NOI18N

        totalMessagesNameVal.setText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.totalMessagesNameVal.text")); // NOI18N

        totalUniqueMessagesNameLabel.setText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.totalUniqueMessagesNameLabel.text")); // NOI18N

        totalUniqueMessagesNameVal.setText(org.openide.util.NbBundle.getMessage(IngestMessagePanel.class, "IngestMessagePanel.totalUniqueMessagesNameVal.text")); // NOI18N

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(sortByLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sortByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(totalMessagesNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalMessagesNameVal, javax.swing.GroupLayout.DEFAULT_SIZE, 9, Short.MAX_VALUE)
                .addGap(22, 22, 22)
                .addComponent(totalUniqueMessagesNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalUniqueMessagesNameVal, javax.swing.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(sortByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sortByLabel)
                .addComponent(totalUniqueMessagesNameLabel)
                .addComponent(totalUniqueMessagesNameVal)
                .addComponent(totalMessagesNameLabel)
                .addComponent(totalMessagesNameVal))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sortByComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByComboBoxActionPerformed
        synchronized (this) {
            if (sortByComboBox.getSelectedIndex() == 0) {
                tableModel.reSort(true);
            } else {
                tableModel.reSort(false);
            }
        }
    }//GEN-LAST:event_sortByComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable messageTable;
    private javax.swing.JComboBox sortByComboBox;
    private javax.swing.JLabel sortByLabel;
    private javax.swing.JLabel totalMessagesNameLabel;
    private javax.swing.JLabel totalMessagesNameVal;
    private javax.swing.JLabel totalUniqueMessagesNameLabel;
    private javax.swing.JLabel totalUniqueMessagesNameVal;
    // End of variables declaration//GEN-END:variables

    private void customizeComponents() {
        mainPanel.setOpaque(true);
        jScrollPane1.setOpaque(true);
        messageTable.setOpaque(false);

        jScrollPane1.setWheelScrollingEnabled(true);

        messageTable.setAutoscrolls(false);
        messageTable.setShowHorizontalLines(false);
        messageTable.setShowVerticalLines(false);

        messageTable.getParent().setBackground(messageTable.getBackground());

        renderer = new MessageTableRenderer();
        int numCols = messageTable.getColumnCount();
        
        // add the cell renderer to all columns
        for (int i = 0; i < numCols; ++i) {
            messageTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        messageTable.setCellSelectionEnabled(false);
        messageTable.setColumnSelectionAllowed(false);
        messageTable.setRowSelectionAllowed(true);
        messageTable.getSelectionModel().addListSelectionListener(new MessageVisitedSelection());
        
        //this should be done at the end to make it easy to initialize before events are handled
        tableModel.addTableModelListener(this);

    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //workaround to force table resize when window resizes.  Use better layout instead?
        setTableSize(messageTable.getParent().getSize());
    }

    @Override
    public void setPreferredSize(Dimension dmnsn) {
        super.setPreferredSize(dmnsn);
        setTableSize(messageTable.getParent().getSize());
    }

    void setTableSize(Dimension d) {
        double[] columnWidths = new double[]{0.20, 0.08, 0.08, 0.49, 0.15};
        int numCols = messageTable.getColumnCount();
        for (int i = 0; i < numCols; ++i) {
            messageTable.getColumnModel().getColumn(i).setPreferredWidth((int)(d.width * columnWidths[i]));
        }
    }

   
    public synchronized void addMessage(IngestMessage m) {
        //final int origMsgUnreadUnique = tableModel.getNumberUnreadGroups();
        tableModel.addMessage(m);

        //update total individual messages count
        ++totalMessages;
        final int newMsgUnreadUnique = tableModel.getNumberUnreadGroups();

        
        try {
            messagePcs.firePropertyChange(TOTAL_NUM_MESSAGES_CHANGED, 0, newMsgUnreadUnique);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "IngestMessagePanel listener threw exception", e);
            MessageNotifyUtil.Notify.show("Module Error", "A module caused an error listening to IngestMessagePanel updates. See log to determine which module. Some data could be incomplete.", MessageNotifyUtil.MessageType.ERROR);
        }

        //update labels
        this.totalMessagesNameVal.setText(Long.toString(totalMessages));
        final int totalMessagesUnique = tableModel.getNumberGroups();
        this.totalUniqueMessagesNameVal.setText(Integer.toString(totalMessagesUnique));
        //this.unreadLabelVal.setText(Integer.toString(newMsgUnreadUnique));

        //autoscroll
        //messageTable.scrollRectToVisible(messageTable.getCellRect(messageTable.getRowCount() - 1, messageTable.getColumnCount(), true));
    }

    public synchronized void clearMessages() {
        final int origMsgGroups = tableModel.getNumberUnreadGroups();
        totalMessages = 0;
        tableModel.clearMessages();
        totalMessagesNameVal.setText("-");
        totalUniqueMessagesNameVal.setText("-");
        
        try {
            messagePcs.firePropertyChange(MESSAGES_BOX_CLEARED, origMsgGroups, 0);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "IngestMessagePanel listener threw exception", e);
            MessageNotifyUtil.Notify.show("Module Error", "A module caused an error listening to IngestMessagePanel updates. See log to determine which module. Some data could be incomplete.", MessageNotifyUtil.MessageType.ERROR);
        }
    }
    
     public synchronized int getMessagesCount() {
         return tableModel.getNumberMessages();
    }

    private synchronized void setVisited(int rowNumber) {
        final int origMsgGroups = tableModel.getNumberUnreadGroups();
        tableModel.setVisited(rowNumber);
        //renderer.setSelected(rowNumber);
        lastRowSelected = rowNumber;
        
        try {
            messagePcs.firePropertyChange(TOOL_TIP_TEXT_KEY, origMsgGroups, tableModel.getNumberUnreadGroups());
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "IngestMessagePanel listener threw exception", e);
            MessageNotifyUtil.Notify.show("Module Error", "A module caused an error listening to IngestMessagePanel updates. See log to determine which module. Some data could be incomplete.", MessageNotifyUtil.MessageType.ERROR);
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int newMessages = tableModel.getNumberNewMessages();
        
        try {
            messagePcs.firePropertyChange(new PropertyChangeEvent(tableModel, TOTAL_NUM_NEW_MESSAGES_CHANGED, -1, newMessages));
        }
        catch (Exception ee) {
            logger.log(Level.SEVERE, "IngestMessagePanel listener threw exception", ee);
            MessageNotifyUtil.Notify.show("Module Error", "A module caused an error listening to IngestMessagePanel updates. See log to determine which module. Some data could be incomplete.", MessageNotifyUtil.MessageType.ERROR);
        }
    }

    private class MessageTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Module", "Num", "New?", "Subject", "Timestamp"};
        private List<TableEntry> messageData = new ArrayList<TableEntry>();
        //for keeping track of messages to group, per module, by uniqness
        private Map<IngestModule, Map<String, List<IngestMessageGroup>>> groupings = new HashMap<IngestModule, Map<String, List<IngestMessageGroup>>>();
        private boolean chronoSort = true; //chronological sort default
        private static final int MESSAGE_GROUP_THRESH = 3; //group messages after 3 messages per module with same uniqness
        private Logger logger = Logger.getLogger(MessageTableModel.class.getName());

        MessageTableModel() {
            init();
        }

        private void init() {
            final IngestManager manager = IngestManager.getDefault();
            //initialize groupings map with modules
            // RJCTODO
//            for (IngestModuleAbstract module : manager.enumerateAbstractFileModules()) {
//                groupings.put(module, new HashMap<String, List<IngestMessageGroup>>());
//            }
//            for (IngestModuleAbstract module : manager.enumerateDataSourceModules()) {
//                groupings.put(module, new HashMap<String, List<IngestMessageGroup>>());
//            }
        }
        
   
        

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        synchronized public int getRowCount() {
            return getNumberGroups();
        }
        
        public void markAllSeen() {
            for (TableEntry entry : messageData) {
                entry.hasBeenSeen(true);
            }
            fireTableChanged(new TableModelEvent(this));
        }
        
        public int getNumberNewMessages() {
            int newMessages = 0;
            for (TableEntry entry : messageData) {
                if (!entry.hasBeenSeen()) {
                    ++newMessages;
                }
            }
            return newMessages;
        }

        synchronized int getNumberGroups() {
            return messageData.size();
        }

        synchronized int getNumberMessages() {
            int total = 0;
            for (TableEntry e : messageData) {
                total += e.messageGroup.getCount();
            }
            return total;
        }

        synchronized int getNumberUnreadMessages() {
            int total = 0;
            for (TableEntry e : messageData) {
                if (!e.hasBeenVisited) {
                    total += e.messageGroup.getCount();
                }
            }
            return total;
        }

        synchronized int getNumberUnreadGroups() {
            int total = 0;
            for (TableEntry e : messageData) {
                if (!e.hasBeenVisited) {
                    ++total;
                }
            }
            return total;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public synchronized Object getValueAt(int rowIndex, int columnIndex) {
            Object ret = null;

            int numMessages = messageData.size();
            if (rowIndex > messageData.size() - 1 ||
                    columnIndex > columnNames.length - 1) {
                //temporary check if the rare case still occurrs
                //#messages is now lower after last regrouping, and gui event thinks it's not
                logger.log(Level.WARNING, "Requested inbox message at" + rowIndex, ", only have " + numMessages);
                return "";
            }
            TableEntry entry = messageData.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    Object module = entry.messageGroup.getSource();
                    ret = module == null ? "" : entry.messageGroup.getSource().getDisplayName();
                    break;
                case 1:
                    ret = entry.messageGroup.getCount();
                    break;
                case 2:
                    ret = !entry.hasBeenSeen();
                    break;
                case 3:
                    ret = entry.messageGroup.getSubject();
                    break;
                case 4:
                    ret = entry.messageGroup.getDatePosted();
                    break;
                default:
                    logger.log(Level.SEVERE, "Invalid table column index: " + columnIndex);
                    break;
            }
            return ret;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        private synchronized int getTableEntryIndex(String uniqueKey) {
            int ret = -1;
            int i = 0;
            for (TableEntry e : messageData) {
                if (e.messageGroup.getUniqueKey().equals(uniqueKey)) {
                    ret = i;
                    break;
                }
                ++i;
            }
            return ret;
        }

        synchronized public void addMessage(IngestMessage m) {
            //check how many messages per module with the same uniqness
            //and add to existing group or create a new group
            IngestModule module = m.getSource();
            IngestMessageGroup messageGroup = null;
            if (module != null && m.getMessageType() == IngestMessage.MessageType.DATA) {
                //not a manager message, a data message, then group
                final Map<String, List<IngestMessageGroup>> groups = groupings.get(module);
                //groups for this uniqueness
                final String uniqueness = m.getUniqueKey();
                List<IngestMessageGroup> uniqGroups = groups.get(uniqueness);
                if (uniqGroups == null) {
                    //first one with this uniqueness
                    uniqGroups = new ArrayList<IngestMessageGroup>();
                    messageGroup = new IngestMessageGroup(m);
                    uniqGroups.add(messageGroup);
                    groups.put(uniqueness, uniqGroups);
                } else {
                    final int uniqueGroupsCount = uniqGroups.size();
                    if (uniqueGroupsCount > MESSAGE_GROUP_THRESH) {
                        //merge them
                        messageGroup = uniqGroups.get(0);
                        for (int i = 1; i < uniqueGroupsCount; ++i) {
                            messageGroup.addAll(uniqGroups.get(i));
                        }
                        //add the new msg
                        messageGroup.add(m);
                        //remove merged groups
                        uniqGroups.clear();

                        //add the group with all messages merged
                        uniqGroups.add(messageGroup);

                        //remove all rows with this uniquness, new merged row will be added to the bottom
                        int toRemove = 0;
                        while ((toRemove = getTableEntryIndex(uniqueness)) != -1) {
                            messageData.remove(toRemove);
                            //remove the row, will be added to the bottom
                            this.fireTableRowsDeleted(toRemove, toRemove);
                        }

                    } else if (uniqueGroupsCount == 1) {
                        IngestMessageGroup first = uniqGroups.get(0);
                        //one group with multiple messages
                        if (first.getCount() > 1) {
                            //had already been merged
                            first.add(m);
                            messageGroup = first;
                            //move to bottom of table
                            //remove from existing position
                            int toRemove = 0;
                            while ((toRemove = getTableEntryIndex(uniqueness)) != -1) {
                                messageData.remove(toRemove);
                                //remove the row, will be added to the bottom
                                this.fireTableRowsDeleted(toRemove, toRemove);
                            }

                        } else {
                            //one group with one message
                            //create another group
                            messageGroup = new IngestMessageGroup(m);
                            uniqGroups.add(messageGroup);

                        }
                    } else {
                        //multiple groups with 1 msg each
                        //create another group, until need to merge
                        messageGroup = new IngestMessageGroup(m);
                        uniqGroups.add(messageGroup);
                        //add to bottom
                    }
                }

            } else {
                //manager or non-data message
                messageGroup = new IngestMessageGroup(m);
            }

            //add new or updated row to the bottom
            messageData.add(new TableEntry(messageGroup));
            int newRowIndex = messageData.size() -1;
            fireTableRowsInserted(newRowIndex, newRowIndex);

            //if priority sort, need to re-sort everything
            if (chronoSort == false) {
                Collections.sort(messageData);
                fireTableDataChanged();
            }
        }

        public synchronized void clearMessages() {
            messageData.clear();
            groupings.clear();
            init();
            fireTableDataChanged();
        }

        public synchronized void setVisited(int rowNumber) {
            messageData.get(rowNumber).hasBeenVisited(true);
            //repaint the cell 
            fireTableCellUpdated(rowNumber, 2);
        }

        public synchronized void setVisitedAll() {
            int row = 0;
            for (TableEntry e : messageData) {
                if (!e.hasBeenVisited) {
                    e.hasBeenVisited(true);
                    fireTableCellUpdated(row, 2);
                }
                ++row;
            }
        }

        public synchronized boolean isVisited(int rowNumber) {
            return messageData.get(rowNumber).hasBeenVisited();
        }

        public synchronized MessageType getMessageType(int rowNumber) {
            return messageData.get(rowNumber).messageGroup.getMessageType();
        }

        public synchronized IngestMessageGroup getMessageGroup(int rowNumber) {
            return messageData.get(rowNumber).messageGroup;
        }

        public synchronized void reSort(boolean chronoLogical) {
            if (chronoSort == chronoLogical) {
                return;
            }

            chronoSort = chronoLogical;
            Collections.sort(messageData);
            fireTableDataChanged();
        }

        class TableEntry implements Comparable<TableEntry> {

            IngestMessageGroup messageGroup;
            boolean hasBeenVisited = false;
            boolean hasBeenSeen = false;

            public boolean hasBeenVisited() {
                return hasBeenVisited;
            }
            
            public void hasBeenVisited(boolean visited) {
                hasBeenVisited = visited;
            }

            public boolean hasBeenSeen() {
                return hasBeenSeen;
            }
            
            public void hasBeenSeen(boolean seen) {
                hasBeenSeen = seen;
            }

            TableEntry(IngestMessageGroup messageGroup) {
                this.messageGroup = messageGroup;
            }

            @Override
            public int compareTo(TableEntry o) {
                if (chronoSort == true) {
                    return this.messageGroup.getDatePosted().compareTo(o.messageGroup.getDatePosted());
                } else {
                    return messageGroup.getCount() - o.messageGroup.getCount();
                }
            }
        }
    }

    //represents grouping of similar messages
    //with the same uniqness
    static class IngestMessageGroup {

        static final Color VERY_HIGH_PRI_COLOR = new Color(164, 164, 202); //for a single message in a group
        static final Color HIGH_PRI_COLOR = new Color(180, 180, 211);
        static final Color MED_PRI_COLOR = new Color(199, 199, 222);
        static final Color LOW_PRI_COLOR = new Color(221, 221, 235);
        private List<IngestMessage> messages;

        IngestMessageGroup(IngestMessage message) {
            messages = new ArrayList<IngestMessage>();
            messages.add(message);
        }

        List<IngestMessage> getMessages() {
            return messages;
        }

        void add(IngestMessage message) {
            messages.add(message);
        }

        //add all messages from another group
        void addAll(IngestMessageGroup group) {
            for (IngestMessage m : group.getMessages()) {
                messages.add(m);
            }
        }

        int getCount() {
            return messages.size();
        }

        String getDetails() {
            StringBuilder b = new StringBuilder("");
            for (IngestMessage m : messages) {
                String details = m.getDetails();
                if (details == null || details.equals("")) {
                    continue;
                }
                b.append(details);
                b.append("<br />");
                b.append("<hr />");
            }

            return b.toString();
        }

        /**
         * return color corresp to priority
         * @return 
         */
        Color getColor() {
            int count = messages.size();
            if (count == 1) {
                return VERY_HIGH_PRI_COLOR;
            } else if (count < 5) {
                return HIGH_PRI_COLOR;
            } else if (count < 15) {
                return MED_PRI_COLOR;
            } else {
                return LOW_PRI_COLOR;
            }

        }

        /**
         * return date of the last message of the group
         * used for chrono sort
         * @return 
         */
        Date getDatePosted() {
            return messages.get(messages.size() - 1).getDatePosted();
        }

        /**
         * get subject of the first message
         * @return 
         */
        String getSubject() {
            return messages.get(0).getSubject();
        }

        /*
         * return unique key, should be the same for all msgs
         */
        String getUniqueKey() {
            return messages.get(0).getUniqueKey();
        }

        /*
         * return source module, should be the same for all msgs
         */
        IngestModule getSource() {
            return messages.get(0).getSource();
        }

        /*
         * return data of the first message
         */
        BlackboardArtifact getData() {
            return messages.get(0).getData();
        }

        /*
         * return message type, should be the same for all msgs
         */
        IngestMessage.MessageType getMessageType() {
            return messages.get(0).getMessageType();
        }
    }
    
    /*
     * Main TableCellRenderer to be used for ingest message inbox. Delegates to
     * other TableCellRenderers based different factors such as column data type
     * or column number.
     */
    private class MessageTableRenderer extends DefaultTableCellRenderer {
        
        private TableCellRenderer booleanRenderer = new BooleanRenderer();
        private TableCellRenderer defaultRenderer = new DefaultRenderer();
        private TableCellRenderer dateRenderer = new DateRenderer();
        protected int rowSelected;

        public void setRowSelected(int rowSelected) {
            this.rowSelected = rowSelected;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                return booleanRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            } else if (value instanceof Date) {
                return dateRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            } else {
                return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
    
    /*
     * TableCellRenderer used to render boolean values with a bullet point.
     */
    private class BooleanRenderer extends DefaultTableCellRenderer {
        
        private final String bulletChar = new String(Character.toChars(0x2022));

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
            
            boolean boolVal;
            if (value instanceof Boolean) {
                boolVal = ((Boolean)value).booleanValue();
            } else {
                throw new RuntimeException("Tried to use BooleanRenderer on non-boolean value.");
            }
            
            String aValue = boolVal ? bulletChar : "";
            
            JLabel cell = (JLabel)super.getTableCellRendererComponent(table, aValue, isSelected, hasFocus, row, column);
            
            // center the bullet in the JLabel
            cell.setHorizontalAlignment(SwingConstants.CENTER);
            
            // increase the font size
            cell.setFont(new Font("", Font.PLAIN, 16));
            
            final IngestMessageGroup messageGroup = tableModel.getMessageGroup(row);
            MessageType mt = messageGroup.getMessageType();
            if (mt == MessageType.ERROR) {
                cell.setBackground(ERROR_COLOR);
            } else if (mt == MessageType.WARNING) {
                cell.setBackground(Color.orange);
            } else {
                //cell.setBackground(table.getBackground());
                cell.setBackground(messageGroup.getColor());
            }
            
            return cell;
        }
        
    }

    /**
     * bold font if not visited, colors for errors
     * tooltips that show entire query string, disable selection borders
     */
    private class DefaultRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            
            Component cell = super.getTableCellRendererComponent(
                    table, value, false, false, row, column);
            
            if (column == 3) {
                String subject = (String)value;
                setToolTipText(subject);
                if (tableModel.isVisited(row)) {
                    cell.setFont(visitedFont);
                } else {
                    cell.setFont(notVisitedFont);
                }
            }

            final IngestMessageGroup messageGroup = tableModel.getMessageGroup(row);
            MessageType mt = messageGroup.getMessageType();
            if (mt == MessageType.ERROR) {
                cell.setBackground(ERROR_COLOR);
            } else if (mt == MessageType.WARNING) {
                cell.setBackground(Color.orange);
            } else {
                //cell.setBackground(table.getBackground());
                cell.setBackground(messageGroup.getColor());
            }

            return cell;
        }
    }
    
    private class DateRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
            
            Object aValue = value;
            if (value instanceof Date) {
                Date date = (Date)value;
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                aValue = df.format(date);
            } else {
                throw new RuntimeException("Tried to use DateRenderer on non-Date value.");
            }
            
            Component cell =  super.getTableCellRendererComponent(table, aValue, isSelected, hasFocus, row, column);
            
            final IngestMessageGroup messageGroup = tableModel.getMessageGroup(row);
            MessageType mt = messageGroup.getMessageType();
            if (mt == MessageType.ERROR) {
                cell.setBackground(ERROR_COLOR);
            } else if (mt == MessageType.WARNING) {
                cell.setBackground(Color.orange);
            } else {
                //cell.setBackground(table.getBackground());
                cell.setBackground(messageGroup.getColor());
            }
            
            return cell;
        }
        
    }

    /**
     * handle table selections / cell visitations
     */
    private class MessageVisitedSelection implements ListSelectionListener {

        private Logger logger = Logger.getLogger(MessageVisitedSelection.class.getName());

        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel selModel = (ListSelectionModel) e.getSource();
            if (selModel.isSelectionEmpty() || selModel.getValueIsAdjusting()) {
                return;
            }

            final int minIndex = selModel.getMinSelectionIndex();
            final int maxIndex = selModel.getMaxSelectionIndex();
            int selected = -1;
            for (int i = minIndex; i <= maxIndex; i++) {
                if (selModel.isSelectedIndex(i)) {
                    selected = i;
                    break;
                }
            }
            selModel.clearSelection();
            if (selected != -1) {
                setVisited(selected);
                messageTable.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //check if has details
                IngestMessageGroup m = getMessageGroup(selected);
                String details = m.getDetails();
                if (details != null && !details.equals("")) {
                    mainPanel.showDetails(selected);
                }
                messageTable.setCursor(null);
            }

        }
    }
}
