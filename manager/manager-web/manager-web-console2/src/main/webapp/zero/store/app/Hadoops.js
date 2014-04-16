/**
 * 
 */

Ext.define('Zero.store.app.Hadoops', {
	extend : 'Zero.store.app.ServerApplications',
	fields : ['group_Id','server_Id', 'server_Name','app_Id', 'app_Ext_Opts' , 'install_Flag' ,'row_Flag','name_Node','secondary_Node','data_Node'],
	constructor : function() {
		var me = this;
		me.callParent(arguments);
		console.log("Hadoop Store Constructor ", me.fields);
		Ext.apply(me.proxy, {
			api : {
				read : '/console2/application/hadoop/list.json',
				update : '/console2/application/hadoop/modify.json',
				create : '/console2/application/hadoop/add.json',
			// destroy : '/console2/application/server/delete.json'
			}
		});
		
	}
});