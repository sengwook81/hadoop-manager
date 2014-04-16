/**
 * 
 */
Ext.define('Zero.controller.app.InitializerCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'app.ServerApplications'
	],
	refs: [{
        ref: 'gridInit',
        selector: '#gridInit'
    }],
	views: ['app.Initializer'],
	init:function () {
		var me = this;
		console.log("InitializerCTR INIT " , me);
		me.control({
			'appInitializer' : {
				afterrender:function () {
					var store = me.getAppServerApplicationsStore();
					store.load({
						params:{
							app_Id:'AA00000018'
						},
						callback:function () {
							console.log("Callback Argu", arguments);
						} 
					});
					me.getGridInit().suspendEvents();
					me.getGridInit().reconfigure(me.getAppServerApplicationsStore());
					me.getGridInit().resumeEvents();
				}
			},
			'checkcolumn': {
				checkchange:function (column,rowIndex,checked) {
					console.log("Grid CHECKCHANGE " , arguments);
					if(column.dataIndex == "name_Node") {
						
					}
				}
			},
			'#gridInit button[text=REFRESH]': {
				click:function () {
					me.getAppServerApplicationsStore().load({params:{
						app_Id:'AA00000018'
					}});
				//	me.getgridServer().reconfigure(me.getBaseServersStore());
				}
			},
			'#gridInit button[text=SAVE]': {
				click:function () {
					me.getAppServerApplicationsStore().sync();
				}
			}
		}
		);
		me.callParent();
	}
});