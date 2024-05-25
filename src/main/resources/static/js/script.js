console.log("this js ");
const toggleSidebar=()=>{
    if($(".sidebar").is(":visible"))
    {
  // true
  // band krna hai
  $(".sidebar").css("display","none");
  $(".content").css("margine-left","0%");
    }
    else{
//  show krna hai
$(".sidebar").css("display","block");
  $(".content").css("margin-left","20%");
    }
};

const Search=()=>{
  //console.log("searching...")
let query = $("#search-input").val();
 console.log(query);
 if(query===""){
$(".search-result").hide();
 }
 else{

  console.log(query);
  let url =  `http://localhost:8282/search/${query}`;
  fetch(url).then((response)=>{
    return response.json();
  }).then((data)=>{
    console.log(data);

    let text =`<div class=' list-group' >`;
    data.forEach(element => {
      text+=`<a href="/user/show-contact/${element.cId}/details" class ='list-group-item list-group-action'>${element.fName}</a>`
    });
    text+=`</div>`;
    $(".search-result").html(text);
    $(".search-result").show();
  })
  
 }
};