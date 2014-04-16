/**
 * 
 */

Ext.define('Zero.store.app.Installers', {
	//extend : 'Ext.data.Store',
	extend : 'Zero.ex.store.ZStore',
	fields:[ 'group_Id','server_Id', 'server_Name','app_Id', 'app_Ext_Opts' , 'install_Flag' , 'install_Status','row_Flag'],
	constructor : function() {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy, {
			api : {
				read : '/console2/application/installer/list.json',
				update : '/console2/application/installer/modify.json',
				create : '/console2/application/installer/add.json',
			// destroy : '/console2/application/server/delete.json'
			}
		});
	}
});