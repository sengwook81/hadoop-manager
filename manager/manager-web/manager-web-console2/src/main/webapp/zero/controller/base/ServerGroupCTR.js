/**
 * 
 */
Ext.define('Zero.controller.base.ServerGroupCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'base.ServerGroups'
	],
	refs: [{
        ref: 'gridServerGroup',
        selector: 'baseServerGroup grid'
    }],
	views: ['base.ServerGroup'],
	init:function () {
		console.log("Load ServerGroupCtr");
		var me = this;
		me.control({
			'baseServerGroup ' : {
				afterrender:function () {
					console.log(me);
					var store = me.getBaseServerGroupsStore();
					store.load(function () {
						
					});
					me.getGridServerGroup().reconfigure(me.getBaseServerGroupsStore());
				}
			},
			'baseServerGroup button[text=REFRESH]': {
				click:function () {
					me.getBaseServerGroupsStore().load({});
				//	me.getGridServerGroup().reconfigure(me.getBaseServerGroupsStore());
				}
			},
			'baseServerGroup button[text=ADD]': {
				click:function () {
					me.getBaseServerGroupsStore().add({});
				}
			},
			'baseServerGroup button[text=SAVE]': {
				click:function () {
					me.getBaseServerGroupsStore().sync();
				}
			},
			'baseServerGroup actioncolumn': {
				click:function (grid , cell , row , col , event, record) {
					alert("Click");
					console.log(record);
					me.getBaseServerGroupsStore().remove(record);
					 //var sm=grid.getSelectionModel();
					/*
	                var sel=sm.getSelections();
	                for(i=0;i<sel.length;i++){
	                    grid.store.remove(sel[i]);
	                }
	                */
				}
			}
		}
		);
		me.callParent();
	}
});