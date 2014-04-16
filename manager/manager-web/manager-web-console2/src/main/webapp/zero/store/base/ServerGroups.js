/**
 * 
 */
Ext.define('Zero.store.base.ServerGroups', {
	extend : 'Zero.ex.store.ZStore',
	fields : [ 'del_Yn','group_Id', 'group_Name', 'group_Desc' ],
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/servergroup/list.json',
				update : '/console2/servergroup/modify.json',
				create : '/console2/servergroup/add.json',
				destroy : '/console2/servergroup/delete.json'
			}
		});
		
	}
});