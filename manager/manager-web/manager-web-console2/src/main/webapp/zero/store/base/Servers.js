/**
 * 
 */

Ext.define('Zero.store.base.Servers', {
	extend : 'Zero.ex.store.ZStore',
	fields : [ 'del_Yn', 'server_Id', 'server_Name', 'host_Info' , 'user_Id' , 'user_Pwd' , 'group_Id'],
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/server/list.json',
				update : '/console2/server/modify.json',
				create : '/console2/server/add.json',
				destroy : '/console2/server/delete.json'
			}
		});
	}
});