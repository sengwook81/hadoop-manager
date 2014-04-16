/**
 * 
 */
Ext.define('Zero.store.desktop.Menus', {
	extend : 'Ext.data.Store',
	storeId : 'desktopMenus',
	fields : [ 'group', 'id', 'name', 'view', 'leaf' ],
	autoLoad : false,
	autoSync : false,
	data : [
	// {group: 'base', name: '서버' , left:false},
	{
		id : 'T_SERVERGROUP',
		group : 'base',
		name : '서버그룹관리',
		view : 'Zero.view.base.ServerGroup',
		left : true
	}, {
		id : 'T_SERVER',
		group : 'base',
		name : '서버관리',
		view : 'Zero.view.base.Server',
		left : true
	},
	// {group: 'base', name: '어플리케이션' , left:false},
	{
		id : 'T_APPMANAGE',
		group : 'base',
		name : '어플리케이션 관리',
		view : 'Zero.view.base.Application',
		left : true
	}, {
		id : 'T_APPINIT',
		group : 'app',
		name : '기본 APP 관리',
		view : 'Zero.view.app.Initializer',
		left : true
	}, {
		id : 'T_APPJAVA',
		group : 'app',
		name : 'Java 서버 관리',
		view : 'Zero.view.app.Java',
		left : true
	}, {
		id : 'T_APPHADOOP',
		group : 'app',
		name : 'Hadoop 서버 관리',
		view : 'Zero.view.app.Hadoop',
		left : true
	}, {
		id : 'T_APPINSTALLER',
		group : 'app',
		name : 'Application Install',
		view : 'Zero.view.app.Installer',
		left : true
	}, ]

});