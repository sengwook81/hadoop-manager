/**
 * 서버 관리 뷰
 */
Ext.define("Zero.view.manage.ServerApplication", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.manageServerApplication',
	requires:['Zero.ex.cmp.ComboboxColumn'],
	controllers:['Zero.controller.manage.ServerApplicationCTR'],
	serverGroupStore:Ext.create('Zero.store.base.ServerGroups'),
	initComponent : function() {
		var me = this;
		me.serverGroupStore.load();
		me.dataGrid = me.buildGrid();
		Ext.apply(me, {
			layout : 'hbox',
			items : [me.dataGrid]
		});
		me.callParent();
	},
	buildGrid:function () {
		var me = this;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		return Ext.create('Ext.grid.Panel',{
			id : 'gridServerApp',
			columns : [ {
				xtype:'zcombobox',
				header : 'SERVER GROUP',
				dataIndex : 'group_Id',
				store:me.serverGroupStore,
				namefield:'group_Name',
				codefield:'group_Id',
			    tooltip: '서버그룹'
			},{
				header : 'NAME',
				dataIndex : 'server_Name',
				flex:1,
				field : {
					xtype : 'textfield'
				}
			}, {
				header : 'HOST',
				dataIndex : 'host_Info',
				flex:1,
				field : {
					xtype : 'textfield'
				}
			},{
				header : 'USERID',
				dataIndex : 'user_Id',
				flex:1,
				field : {
					xtype : 'textfield'
				}
			}],
			plugins : [ cellEditingGrid ],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock:'top',
				items : [ {
					text : 'REFRESH',
					xtype:'button',
				},'->', {
					text : 'ADD',
					xtype:'button',
				}, {
					text : 'SAVE',
					xtype:'button',
				} ]
			} ]
		});
	}
});