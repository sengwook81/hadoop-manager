/**
 * 
 */
Ext.define('Zero.controller.manage.ServerApplicationCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'manage.ServerApplications'
	],
	refs: [{
        ref: 'gridServerApp',
        selector: '#gridServerApp'
    }],
	views: ['manage.ServerApplication'],
	init:function () {
		console.log("Load ServerApplicationCTR");
		var me = this;
		me.control({
			'manageServerApplication' : {
				afterrender:function () {
					var store = me.getManageServerApplicationsStore();
					store.load(function () {
						console.log("Server Data Load");
					});
					me.getGridServerApp().suspendEvents();
					me.getGridServerApp().reconfigure(me.getManageServerApplicationsStore());
					me.getGridServerApp().resumeEvents();
				}
			},
			'gridServerApp button[text=REFRESH]': {
				click:function () {
					me.getBaseServersStore().load({});
				//	me.getgridServer().reconfigure(me.getBaseServersStore());
				}
			},
			'gridServerApp button[text=ADD]': {
				click:function () {
					me.getBaseServersStore().add({});
					console.log(me.getBaseServersStore());
				}
			},
			'gridServerApp button[text=SAVE]': {
				click:function () {
					me.getBaseServersStore().sync();
				}
			}
		}
		);
		me.callParent();
	}
});