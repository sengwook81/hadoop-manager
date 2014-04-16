/**
 * 서버 관리 뷰
 */
Ext.define("Zero.view.base.Server", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.baseServer',
	requires:['Zero.store.base.Servers' , 'Zero.ex.cmp.ComboboxColumn'],
	controllers:['Zero.controller.base.ServerCTR'],
	initComponent : function() {
		var me = this;
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
		var serverGroupStore = Ext.create('Zero.store.base.ServerGroups');
		serverGroupStore.load();
		return Ext.create('Ext.grid.Panel',{
			id : 'gridServer',
			store: Ext.create('Zero.store.base.Servers'),
			columns : [ {
				header : 'Name',
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
			},{
				header : 'PASSWORD',
				dataIndex : 'user_Pwd',
				flex:1,
				field : {
					xtype : 'textfield'
				},
				tooltip: '패스워드'
			},{
				xtype:'zcombobox',
				header : 'ServerGroup',
				dataIndex : 'group_Id',
				store:serverGroupStore,
				namefield:'group_Name',
				codefield:'group_Id',
				editable:true,
			    tooltip: '서버그룹'
			},{
				xtype: 'checkcolumn',
				dataIndex : 'del_Yn',
				stopSelection : false,
				header: 'Remove',
				width: 90
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