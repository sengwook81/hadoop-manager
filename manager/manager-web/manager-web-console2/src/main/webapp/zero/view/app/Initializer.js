/**
 * 서버 관리 뷰
 */
Ext.define("Zero.view.app.Initializer", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.appInitializer',
	requires : [ 'Zero.ex.cmp.ComboboxColumn' ,'Zero.store.base.InstallStatus','Zero.ex.grid.BoolCheckColumn'],
	controllers : [ 'Zero.controller.app.InitializerCTR' ],
	serverGroupStore : Ext.create('Zero.store.base.ServerGroups'),
	initComponent : function() {
		var me = this;
		me.serverGroupStore.load({
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
			id : 'gridInit',
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
				xtype : 'boolcheckcolumn',
				header : 'INSTALL',
				checkVal:'install',
				uncheckVal : '',
				dataIndex : 'app_Ext_Opts',
				flex : 1
			}, {
				xtype : 'zcombobox',
				header : 'STATUS',
				dataIndex : 'install_Flag',
				store : Ext.create('Zero.store.base.InstallStatus'),
				namefield : 'name',
				codefield : 'code',
				tooltip : '설치상태',
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