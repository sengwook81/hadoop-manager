/**
 * 
 */
Ext.define("Zero.view.base.ServerGroup", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.baseServerGroup',
	requires:['Zero.store.base.ServerGroups'],
	controllers:['Zero.controller.base.ServerGroupCTR'],
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
		return Ext.create('Ext.grid.Panel',{
			id : 'gridServerGroup',
			store: Ext.create('Zero.store.base.ServerGroups'),
			columns : [ {
				header : 'NAME',
				dataIndex : 'group_Name',
				field : {
					xtype : 'textfield'
				}
			}, {
				header : 'DESC',
				dataIndex : 'group_Desc',
				flex:1,
				field : {
					xtype : 'textfield'
				}
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