/**
 * 
 */

Ext.define('Zero.store.app.ServerApplications', {
	extend : 'Zero.ex.store.ZStore',
	fields : ['group_Id','server_Id', 'server_Name','app_Id', 'app_Ext_Opts' , 'install_Flag', 'install_Status' ,'row_Flag','install_Chk'],
	pageSize : 10,
	constructor : function() {
		var me = this;
		me.callParent(arguments);
		console.log("Installers Store Constructor ", me.fields);
		Ext.apply(me.proxy, {
			api : {
				read : '/console2/application/server/list.json',
				update : '/console2/application/server/modify.json',
				create : '/console2/application/server/add.json',
			}
		});
		
	}
});