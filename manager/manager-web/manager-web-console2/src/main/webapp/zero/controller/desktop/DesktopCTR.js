/**
 * 
 */
Ext.define('Zero.controller.desktop.DesktopCTR', {
	extend : 'Ext.app.Controller',
	stores : [ 'desktop.Menus' ],
	refs : [ {
		ref : 'leftMenu',
		selector : 'desktopLeftMenu'
	}, {
		ref : 'contentTab',
		selector : 'desktopContentTab'
	}, ],
	views : [ 'desktop.LeftMenu', 'desktop.ContentTab' ],
	init : function() {
		console.log("Load DesktopCTR");
		var me = this;
		me.control({
			'desktopLeftMenu ' : {
				afterrender : function(el) {
					var menuStore = me.getDesktopMenusStore();
					console.log(el, me.getDesktopMenusStore());
					//alert("Menu Rendered");
					// Left Menu Tree 생성.
					el.items.each(function(item) {
						console.log("Item :", item, item.group);
						menuStore.clearFilter(true); // 필터 해제.
						// 메뉴그룹별로 필터
						me.getDesktopMenusStore().filter('group', item.group);
						// 메뉴 Store Item Loop
						me.getDesktopMenusStore().each(function(record) {
							// 메뉴 등록.
							item.getRootNode().appendChild({
								text : record.get('name'),
								leaf : true,//record.get('leaf'),
								view : record.get('view'),
								id : record.get('id'),
							});
						});
					});
				}
			},
			'desktopLeftMenu desktopLeftMenuItem' : {
				select : function(el, record) {
					console.log("Tree Leaf Select", el, record);
					console.log("content Tab",me.getContentTab(), record.get("id") , record.raw.view);
					// 탭에 해당 메뉴 View 추가.
					var contentTab = me.getContentTab().child('#'+ record.get("id"));
					console.log("Registered Tab ", contentTab);
					if(contentTab == null) {
						// 추가 안되어있는 경우.
						console.log("Add Child Tab",record.get("id"));
						childTab =  me.getContentTab().add(Ext.create(record.raw.view,{
							closable:true,
							itemId:record.get('id'),
							title:record.get('text')
						}));
					}
					me.getContentTab().setActiveTab(record.get("id"));
				}
			}

		});
		me.callParent();
	}
});