/**
 * 서버 관리 뷰
 */
Ext.define("Zero.view.app.Hadoop", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.appHadoop',
	requires : [ 'Zero.ex.cmp.ComboboxColumn','Zero.store.base.InstallStatus' ],
	controllers : [ 'Zero.controller.app.HadoopCTR' ],
	serverGroupStore : Ext.create('Zero.store.base.ServerGroups'),
	initComponent : function() {
		var me = this;
		me.serverGroupStore.load({
			callback : function() {
			}
		});
		me.dataGrid = me.buildGrid();
		Ext.apply(me, {
			layout : 'hbox',
			items : [ me.dataGrid ]
		});
		me.callParent();
	},
	buildGrid : function() {
		var me = this;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			id:'gridHadoopEdit',
			clicksToEdit : 1
		});
		return Ext.create('Ext.grid.Panel', {
			id : 'gridHadoop',
			columns : [ {
				xtype : 'zcombobox',
				header : 'SERVER GROUP',
				dataIndex : 'group_Id',
				store : me.serverGroupStore,
				namefield : 'group_Name',
				codefield : 'group_Id',
				tooltip : '서버그룹',
			}, {
				header : 'NAME',
				dataIndex : 'server_Name',
				flex : 1,
			}, {
				xtype : 'checkcolumn',
				header : 'NAME',
				dataIndex : 'name_Node',
				flex : 1
			},{
				xtype : 'checkcolumn',
				header : 'SECONDARY',
				dataIndex : 'secondary_Node',
				flex : 1
			},{
				xtype : 'checkcolumn',
				header : 'DATA',
				dataIndex : 'data_Node',
				flex : 1
			}, {
				xtype : 'zcombobox',
				header : 'STATUS',
				dataIndex : 'install_Flag',
				store : Ext.create('Zero.store.base.InstallStatus'),
				namefield : 'name',
				codefield : 'code',
				tooltip : '설치상태',
				flex : 1
			} ],
			plugins : [ cellEditingGrid ],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					text : 'REFRESH',
					xtype : 'button',
				}, '->', {
					text : 'SAVE',
					xtype : 'button',
				} ]
			} ]
		});
	}
});