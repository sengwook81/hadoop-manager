/**
 * 
 */

Ext.define('Zero.store.app.MonitorProcess', {
	//extend : 'Ext.data.Store',
	extend : 'Zero.ex.store.ZStore',
	//, 'process_Status' , 'process_Data'
	fields:[ 'group_Id','server_Id', 'server_Name','app_Id', 'process_Status' , 'process_Data','install_Chk'],
	constructor : function() {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy, {
			api : {
				read : '/console2/application/installer/monitor.json',
			// destroy : '/console2/application/server/delete.json'
			}
		});
	}
});