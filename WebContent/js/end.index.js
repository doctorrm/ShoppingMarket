//隐藏和显示
$(document).ready(function(){	
	$(".h-right-div").hide();//hiden first for following apperance.	
	$("#hidden_id_text").hide();
	$(".h-news-div").hide();
    $(".btn-add").click(function(){
		$(".right-div").hide();
		$(".h-right-div").show();	
	}) 
	$(".news").click(function(){
		$(".right-div").hide();
		$(".h-right-div").hide();
		$(".h-news-div").show();
		document.getElementById("a1").className="#";	
		document.getElementById("a4").className="active";
	})
})

function updateFun(id,name,description,price) {//在调用方法传入参数时要加引号，否则无法出发。
	//原因可能是如果不加引号就会把传入的参数当作变量而不是字符串或什么具体类型的数据。而删除函数的参数在被调用传入参数时则不用加引号，因为传入的是id即数字，能认出来。
		//alert("Update alert!");
		//alert(id);
	document.getElementById("good_name").value=name;
	document.getElementById("good_description").value=description;
	document.getElementById("good_price").value=price;
	document.getElementById("hidden_id_text").value=id;
	$(".right-div").hide();
	$(".h-right-div").show();
}//end updateFun() function


function deleteFun(idInt) {
	var idStr=idInt.toString();//这里传入的idInt是数字，要转换为字符串
	jsonStr="{'deleteSignal':'"+idStr+"'}";
	$.ajax({
		type:"post",
		url:"SelAndDelServlet",
		data:jsonStr,
		success:function(){				
			//alert("success");
		},
		error:function(){
			//alert("error");
		}
	}); //end ajax	
	
	setTimeout(function() {
		location.reload(); //这句话不要单独使用
	}, 50);
}//end deleteFun() function

function chkfrm() {//校检表单
	var nameStr=document.getElementById("good_name").value;
	var descriptionStr=document.getElementById("good_description").value;
	var priceStr=document.getElementById("good_price").value;
	var priceInt=Number(priceStr,10);//要加第二个参数（转化为10进制），避免因为输入010（开头为0时默认为8进制）出错。
	var picStr= document.getElementById("good_main_pic").value;	
	var picsStr=document.getElementById("good_desc_pics").value;
		
	if (nameStr==""||nameStr==null) {//实际上提交的是空字符串，所以null其实可以不用考虑
		alert("商品名称不能为空!");
		return false;
	} //第一个if如果过不了，下面的if就都不会被执行了。
	if(descriptionStr==""){
		alert("对不起,请输入商品的简单描述");
		return false;
	} 
 	if(priceStr==""){ 		
 		alert("对不起,请输入商品价格!");
		return false;
	}else if (isNaN(priceInt)) {//虽然价格是以字符串形式提交，但是后台会把它转为数字，所以在前端要检验是否可以转为数字格式先。。。
		//如果不是数字就返回true。NaN大小写区分。		
		alert("您输入的价格不是数字!");
		return false;
	} 
 	if(picStr==""){
 		alert("请上传商品的封面图片");
 		return false;
 	}
 	if(picsStr==""){
 		alert("请上传商品的至少一张详情图片");
 		return false;
 	}
	return true;//默认可以提交
}



	//页面计时器
	/*window.onload = startInterval;
	function startInterval()
	{
	    setInterval("startTime();",1000);
	}
	
	function startTime()
	{
	    document.getElementById("time").innerHTML = Date();//好像必须放在对于的div下面才能根据id获取到id  
	}*/

