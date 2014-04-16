/**
 * 
 */
Ext.define('Zero.controller.app.JavaCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'app.ServerApplications'
	],
	refs: [{
        ref: 'gridJava',
        selector: '#gridJava'
    }],
	views: ['app.Java'],
	init:function () {
		var me = this;
		console.log("JavaCTR INIT " , me);
		me.control({
			'appJava' : {
				afterrender:function () {
					var store = me.getAppServerApplicationsStore();
					store.load({
						params:{
							app_Id:'AA00000017'
						},
						callback:function () {
							console.log("Callback Argu", arguments);
						} 
					});
					me.getGridJava().suspendEvents();
					me.getGridJava().reconfigure(me.getAppServerApplicationsStore());
					me.getGridJava().resumeEvents();
				}
			},
			'checkcolumn': {
				checkchange:function (column,rowIndex,checked) {
					console.log("Grid CHECKCHANGE " , arguments);
					if(column.dataIndex == "name_Node") {
						
					}
				}
			},
			'#gridJava button[text=REFRESH]': {
				click:function () {
					me.getAppServerApplicationsStore().load({params:{
						app_Id:'AA00000017'
					}});
				//	me.getgridServer().reconfigure(me.getBaseServersStore());
				}
			},
			'#gridJava button[text=SAVE]': {
				click:function () {
					me.getAppServerApplicationsStore().sync();
				}
			}
		}
		);
		me.callParent();
	}
});