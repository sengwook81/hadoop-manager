/**
 * 
 */

Ext.define('Zero.store.app.AppConfigs', {
	extend : 'Zero.ex.store.ZStore',
	fields : [ 'del_Yn','app_Id', 'config_Id', 'prop_Key', 'prop_Val' ,'prop_Desc' , 'prop_Type'],
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/application/config/ext/list.json',
				update : '/console2/application/config/ext/modify.json',
				create : '/console2/application/config/ext/add.json',
				destroy : '/console2/application/config/ext/delete.json'
			}
		});
		
	}
});