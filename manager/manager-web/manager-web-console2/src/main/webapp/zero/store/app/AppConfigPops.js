/**
 * 
 */

Ext.define('Zero.store.app.AppConfigPops', {
	extend : 'Zero.ex.store.ZStore',
	fields : [ 'del_Yn','app_Id', 'config_Id', 'prop_Key', 'prop_Val' ,'prop_Desc' , 'prop_Type' ,'chk'],
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/application/config/ext/pop/list.json',
				update : '/console2/application/config/ext/pop/modify.json',
			}
		});
		
	}
});