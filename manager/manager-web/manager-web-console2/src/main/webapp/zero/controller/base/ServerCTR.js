/**
 * 
 */
Ext.define('Zero.controller.base.ServerCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'base.Servers'
	],
	refs: [{
        ref: 'gridServer',
        selector: 'baseServer grid'
    }],
	views: ['base.Server'],
	init:function () {
		console.log("Load ServerCTR");
		var me = this;
		me.control({
			'baseServer' : {
				afterrender:function () {
					var store = me.getBaseServersStore();
					store.load({callback:function () {
						console.log("Server Data Load");
					}});
					me.getGridServer().suspendEvents();
					me.getGridServer().reconfigure(me.getBaseServersStore());
					me.getGridServer().resumeEvents();
				}
			},
			'baseServer button[text=REFRESH]': {
				click:function () {
					me.getBaseServersStore().load({});
				//	me.getgridServer().reconfigure(me.getBaseServersStore());
				}
			},
			'baseServer button[text=ADD]': {
				click:function () {
					me.getBaseServersStore().add({});
					console.log(me.getBaseServersStore());
				}
			},
			'baseServer button[text=SAVE]': {
				click:function () {
					me.getBaseServersStore().sync();
				}
			}
		}
		);
		me.callParent();
	}
});