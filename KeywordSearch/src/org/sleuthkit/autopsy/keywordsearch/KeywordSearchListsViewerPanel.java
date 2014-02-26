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
package org.sleuthkit.autopsy.keywordsearch;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestManager;
import org.sleuthkit.autopsy.ingest.IngestManager.IngestModuleEvent;

/**
 * Viewer panel widget for keyword lists that is used in the ingest config and options area.
 */
class KeywordSearchListsViewerPanel extends AbstractKeywordSearchPerformer {

    private static final Logger logger = Logger.getLogger(KeywordSearchListsViewerPanel.class.getName());
    private static KeywordSearchListsViewerPanel instance;
    private KeywordSearchListsXML loader;
    private KeywordListsTableModel listsTableModel;
    private KeywordsTableModel keywordsTableModel;
    private ActionListener searchAddListener;
    private boolean ingestRunning;

    /**
     * Creates new form KeywordSearchListsViewerPanel
     */
    private KeywordSearchListsViewerPanel() {
        listsTableModel = new KeywordListsTableModel();
        keywordsTableModel = new KeywordsTableModel();
        initComponents();
        customizeComponents();
    }

    static synchronized KeywordSearchListsViewerPanel getDefault() {
        if (instance == null) {
            instance = new KeywordSearchListsViewerPanel();
        }
        return instance;
    }

    private void customizeComponents() {
        listsTable.setTableHeader(null);
        listsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //customize column witdhs
        final int leftWidth = leftPane.getPreferredSize().width;
        TableColumn column = null;
        for (int i = 0; i < listsTable.getColumnCount(); i++) {
            column = listsTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(((int) (leftWidth * 0.10)));
                column.setCellRenderer(new LeftCheckBoxRenderer());
            } else {
                column.setPreferredWidth(((int) (leftWidth * 0.89)));
            }
        }
        final int rightWidth = rightPane.getPreferredSize().width;
        for (int i = 0; i < keywordsTable.getColumnCount(); i++) {
            column = keywordsTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(((int) (rightWidth * 0.78)));
            } else {
                column.setPreferredWidth(((int) (rightWidth * 0.20)));
                column.setCellRenderer(new RightCheckBoxRenderer());
            }
        }

        loader = KeywordSearchListsXML.getCurrent();
        listsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int index = listSelectionModel.getMinSelectionIndex();
                    KeywordSearchListsAbstract.KeywordSearchList list = listsTableModel.getListAt(index);
                    keywordsTableModel.resync(list);
                } else {
                    keywordsTableModel.deleteAll();
                }
            }
        });

        final KeywordSearchIngestModule module = KeywordSearchIngestModule.getDefault();
        if (IngestManager.getDefault().isIngestRunning()) {
            initIngest(true);
        } else {
            initIngest(false);
        }

        IngestManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String changed = evt.getPropertyName();
                Object oldValue = evt.getOldValue();
                if (changed.equals(IngestModuleEvent.COMPLETED.toString())
                        && ((String) oldValue).equals(KeywordSearchIngestModule.MODULE_NAME)) {
                    initIngest(false);
                } else if (changed.equals(IngestModuleEvent.STARTED.toString())
                        && ((String) oldValue).equals(KeywordSearchIngestModule.MODULE_NAME)) {
                    initIngest(true);
                } else if (changed.equals(IngestModuleEvent.STOPPED.toString())
                        && ((String) oldValue).equals(KeywordSearchIngestModule.MODULE_NAME)) {
                    initIngest(false);
                }
            }
        });

        searchAddListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ingestRunning) {
                    module.addKeywordLists(listsTableModel.getSelectedLists());
                    logger.log(Level.INFO, "Submitted enqueued lists to ingest");
                } else {
                    searchAction(e);
                }
            }
        };

        searchAddButton.addActionListener(searchAddListener);

    }

    /**
     * Initialize this panel depending on whether ingest is running
     *
     * @param running case 0: ingest running case 1: ingest not running
     */
    private void initIngest(boolean running) {
        if (running) {
            ingestRunning = true;
            searchAddButton.setText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.addIngestTitle"));
            searchAddButton.setToolTipText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.addIngestMsg" ));
            listsTableModel.resync();
            
        } else {
            ingestRunning = false;
            searchAddButton.setText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.searchIngestTitle"));
            searchAddButton.setToolTipText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.addIdxSearchMsg"));
            listsTableModel.resync();
        }
        updateIngestIndexLabel(running);
    }
    
    private void updateIngestIndexLabel(boolean ingestRunning) {
        if (ingestRunning) {
            ingestIndexLabel.setText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.ongoingIngestMsg", filesIndexed));
        }
        else {
            ingestIndexLabel.setText(NbBundle.getMessage(this.getClass(), "KeywordSearchListsViewerPanel.initIngest.fileIndexCtMsg", filesIndexed));
        }
    }
    
    @Override
    protected void postFilesIndexedChange() {
         updateIngestIndexLabel(ingestRunning);
    }

    /**
     * Force resync the data view
     */
    void resync() {
        listsTableModel.resync();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        leftPane = new javax.swing.JScrollPane();
        listsTable = new javax.swing.JTable();
        rightPane = new javax.swing.JScrollPane();
        keywordsTable = new javax.swing.JTable();
        manageListsButton = new javax.swing.JButton();
        searchAddButton = new javax.swing.JButton();
        ingestIndexLabel = new javax.swing.JLabel();

        leftPane.setMinimumSize(new java.awt.Dimension(150, 23));

        listsTable.setBackground(new java.awt.Color(240, 240, 240));
        listsTable.setModel(listsTableModel);
        listsTable.setShowHorizontalLines(false);
        listsTable.setShowVerticalLines(false);
        listsTable.getTableHeader().setReorderingAllowed(false);
        leftPane.setViewportView(listsTable);

        jSplitPane1.setLeftComponent(leftPane);

        keywordsTable.setBackground(new java.awt.Color(240, 240, 240));
        keywordsTable.setModel(keywordsTableModel);
        keywordsTable.setShowHorizontalLines(false);
        keywordsTable.setShowVerticalLines(false);
        rightPane.setViewportView(keywordsTable);

        jSplitPane1.setRightComponent(rightPane);

        manageListsButton.setText(org.openide.util.NbBundle.getMessage(KeywordSearchListsViewerPanel.class, "KeywordSearchListsViewerPanel.manageListsButton.text")); // NOI18N
        manageListsButton.setToolTipText(org.openide.util.NbBundle.getMessage(KeywordSearchListsViewerPanel.class, "KeywordSearchListsViewerPanel.manageListsButton.toolTipText")); // NOI18N
        manageListsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageListsButtonActionPerformed(evt);
            }
        });

        searchAddButton.setText(org.openide.util.NbBundle.getMessage(KeywordSearchListsViewerPanel.class, "KeywordSearchListsViewerPanel.searchAddButton.text")); // NOI18N

        ingestIndexLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ingestIndexLabel.setText(org.openide.util.NbBundle.getMessage(KeywordSearchListsViewerPanel.class, "KeywordSearchListsViewerPanel.ingestIndexLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(searchAddButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                        .addComponent(manageListsButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ingestIndexLabel)
                        .addGap(0, 317, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(ingestIndexLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manageListsButton)
                    .addComponent(searchAddButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void manageListsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageListsButtonActionPerformed
        SystemAction.get(KeywordSearchConfigurationAction.class).performAction();
    }//GEN-LAST:event_manageListsButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ingestIndexLabel;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable keywordsTable;
    private javax.swing.JScrollPane leftPane;
    private javax.swing.JTable listsTable;
    private javax.swing.JButton manageListsButton;
    private javax.swing.JScrollPane rightPane;
    private javax.swing.JButton searchAddButton;
    // End of variables declaration//GEN-END:variables

    private void searchAction(ActionEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            search();
        } finally {
            setCursor(null);
        }
    }

    @Override
    public List<Keyword> getQueryList() {
        List<Keyword> ret = new ArrayList<Keyword>();
        for (KeywordSearchListsAbstract.KeywordSearchList list : getSelectedLists()) {
            ret.addAll(list.getKeywords());
        }
        return ret;
    }

    private List<KeywordSearchListsAbstract.KeywordSearchList> getSelectedLists() {
        return listsTableModel.getSelectedListsL();
    }

    @Override
    public boolean isMultiwordQuery() {
        return true;
    }

    @Override
    public boolean isLuceneQuerySelected() {
        throw new UnsupportedOperationException("Not supported for multi-word queries.");
    }

    @Override
    public String getQueryText() {
        throw new UnsupportedOperationException("Not supported for multi-word queries.");
    }

    void addSearchButtonActionListener(ActionListener al) {
        searchAddButton.addActionListener(al);
    }

    private class KeywordListsTableModel extends AbstractTableModel {
        //data

        private KeywordSearchListsXML listsHandle = KeywordSearchListsXML.getCurrent();
        private List<ListTableEntry> listData = new ArrayList<ListTableEntry>();

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return listData.size();
        }

        @Override
        public String getColumnName(int column) {
            String ret = null;
            switch (column) {
                case 0:
                    ret = NbBundle.getMessage(this.getClass(), "KeywordSearch.selectedColLbl");
                    break;
                case 1:
                    ret = NbBundle.getMessage(this.getClass(), "KeywordSearch.nameColLbl");
                    break;
                default:
                    break;
            }
            return ret;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object ret = null;
            ListTableEntry entry = null;
            //iterate until row
            Iterator<ListTableEntry> it = listData.iterator();
            for (int i = 0; i <= rowIndex; ++i) {
                entry = it.next();
            }
            switch (columnIndex) {
                case 0:
                    ret = (Object) entry.selected;
                    break;
                case 1:
                    ret = (Object) entry.name;
                    break;
                default:
                    break;
            }
            return ret;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            List<String> locked = KeywordSearchIngestModule.getDefault().getKeywordLists();
            return (columnIndex == 0 && (!ingestRunning || !locked.contains((String) getValueAt(rowIndex, 1))));
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                ListTableEntry entry = null;
                Iterator<ListTableEntry> it = listData.iterator();
                for (int i = 0; i <= rowIndex; i++) {
                    entry = it.next();
                }
                if (entry != null) {
                    entry.selected = (Boolean) aValue;
                    if (ingestRunning) {
                        //updateUseForIngest(getListAt(rowIndex), (Boolean) aValue);
                    }
                }

            }
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        List<String> getAllLists() {
            List<String> ret = new ArrayList<String>();
            for (ListTableEntry e : listData) {
                ret.add(e.name);
            }
            return ret;
        }

        KeywordSearchListsAbstract.KeywordSearchList getListAt(int rowIndex) {
            return listsHandle.getList((String) getValueAt(rowIndex, 1));
        }

        List<String> getSelectedLists() {
            List<String> ret = new ArrayList<String>();
            for (ListTableEntry e : listData) {
                if (e.selected) {
                    ret.add(e.name);
                }
            }
            return ret;
        }

        List<KeywordSearchListsAbstract.KeywordSearchList> getSelectedListsL() {
            List<KeywordSearchListsAbstract.KeywordSearchList> ret = new ArrayList<KeywordSearchListsAbstract.KeywordSearchList>();
            for (String s : getSelectedLists()) {
                ret.add(listsHandle.getList(s));
            }
            return ret;
        }

        boolean listExists(String list) {
            List<String> all = getAllLists();
            return all.contains(list);
        }

        //resync model from handle, then update table
        void resync() {
            listData.clear();
            addLists(listsHandle.getListsL());
            fireTableDataChanged();
        }

        //add lists to the model
        private void addLists(List<KeywordSearchListsAbstract.KeywordSearchList> lists) {
            for (KeywordSearchListsAbstract.KeywordSearchList list : lists) {
                if (!listExists(list.getName())) {
                    listData.add(new ListTableEntry(list, ingestRunning));
                }
            }
        }

        //single model entry
        private class ListTableEntry implements Comparable<ListTableEntry> {

            String name;
            Boolean selected;

            ListTableEntry(KeywordSearchListsAbstract.KeywordSearchList list, boolean ingestRunning) {
                this.name = list.getName();
                if (ingestRunning) {
                    this.selected = list.getUseForIngest();
                } else {
                    this.selected = false;
                }
            }

            @Override
            public int compareTo(ListTableEntry e) {
                return this.name.compareTo(e.name);
            }
        }
    }

    private class KeywordsTableModel extends AbstractTableModel {

        List<KeywordTableEntry> listData = new ArrayList<KeywordTableEntry>();

        @Override
        public int getRowCount() {
            return listData.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            String ret = null;
            switch (column) {
                case 0:
                    ret = NbBundle.getMessage(this.getClass(), "KeywordSearch.nameColLbl");
                    break;
                case 1:
                    ret = NbBundle.getMessage(this.getClass(), "KeywordSearch.regExColLbl");
                    break;
                default:
                    break;
            }
            return ret;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object ret = null;
            KeywordTableEntry entry = null;
            //iterate until row
            Iterator<KeywordTableEntry> it = listData.iterator();
            for (int i = 0; i <= rowIndex; ++i) {
                entry = it.next();
            }
            switch (columnIndex) {
                case 0:
                    ret = (Object) entry.name;
                    break;
                case 1:
                    ret = (Object) entry.regex;
                    break;
                default:
                    break;
            }
            return ret;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        void resync(KeywordSearchListsAbstract.KeywordSearchList list) {
            listData.clear();
            for (Keyword k : list.getKeywords()) {
                listData.add(new KeywordTableEntry(k));
            }
            fireTableDataChanged();
        }

        void deleteAll() {
            listData.clear();
            fireTableDataChanged();
        }

        //single model entry
        private class KeywordTableEntry implements Comparable<KeywordTableEntry> {

            String name;
            Boolean regex;

            KeywordTableEntry(Keyword keyword) {
                this.name = keyword.getQuery();
                this.regex = !keyword.isLiteral();
            }

            @Override
            public int compareTo(KeywordTableEntry e) {
                return this.name.compareTo(e.name);
            }
        }
    }

    private class LeftCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            this.setHorizontalAlignment(JCheckBox.CENTER);
            this.setVerticalAlignment(JCheckBox.CENTER);

            String name = (String) table.getModel().getValueAt(row, 1);
            List<String> currentIngest = KeywordSearchIngestModule.getDefault().getKeywordLists();
            boolean currentIngestUsed = currentIngest.contains(name);
            setEnabled(!currentIngestUsed || !ingestRunning);

            boolean selected = (Boolean) table.getModel().getValueAt(row, 0);
            setSelected(selected);

            if (isSelected) {
                setBackground(listsTable.getSelectionBackground());
            } else {
                setBackground(listsTable.getBackground());
            }

            return this;
        }
    }

    private class RightCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            this.setHorizontalAlignment(JCheckBox.CENTER);
            this.setVerticalAlignment(JCheckBox.CENTER);

            Boolean selected = (Boolean) table.getModel().getValueAt(row, 1);
            setSelected(selected);
            if (isSelected) {
                setBackground(keywordsTable.getSelectionBackground());
            } else {
                setBackground(keywordsTable.getBackground());
            }
            setEnabled(false);

            return this;
        }
    }
}
