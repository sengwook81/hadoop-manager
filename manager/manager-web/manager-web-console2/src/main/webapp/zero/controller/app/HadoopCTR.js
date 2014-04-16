/**
 * 
 */
Ext.define('Zero.controller.app.HadoopCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'app.Hadoops'
	],
	refs: [{
        ref: 'gridHadoop',
        selector: '#gridHadoop'
    }],
	views: ['app.Hadoop'],
	init:function () {
		console.log("Load HadoopCTR");
		var me = this;
		me.control({
			'appHadoop' : {
				afterrender:function () {
					var store = me.getAppHadoopsStore();
					store.load({
						params:{
							app_Id:'AA00000014'
						},
						callback:function () {
							console.log("Callback Argu", arguments);
						} 
					});
					me.getGridHadoop().suspendEvents();
					me.getGridHadoop().reconfigure(me.getAppHadoopsStore());
					me.getGridHadoop().resumeEvents();
				}
			},
			'checkcolumn': {
				checkchange:function (column,rowIndex,checked) {
					console.log("Grid CHECKCHANGE " , arguments);
					if(column.dataIndex == "name_Node") {
						
					}
				}
			},
			'#gridHadoop button[text=REFRESH]': {
				click:function () {
					me.getAppHadoopsStore().load({params:{
						app_Id:'AA00000014'
					}});
				//	me.getgridServer().reconfigure(me.getBaseServersStore());
				}
			},
			'#gridHadoop button[text=SAVE]': {
				click:function () {
					me.getAppHadoopsStore().sync();
				}
			}
		}
		);
		me.callParent();
	}
});