document.getElementsByClassName("layout__header mhead")[0].style.display = "none";
document.getElementsByClassName("layout__leftMenu")[0].style.display = "none";
document.getElementsByClassName("basis__header mhead")[0].style.display = "none";
document.getElementsByClassName("basis__footer mfoot")[0].style.display = "none";
document.getElementsByClassName("groupCover")[0].style.display = "none";
document.getElementsByClassName("basisGroup__main owner_panel profile_panel")[0].style.display = "none";
document.getElementsByClassName("profile_info")[0].style.display = "none";
document.getElementsByClassName("profile_info")[1].style.display = "none";
document.getElementsByClassName("profile_info")[2].style.display = "none";
document.getElementsByClassName("slim_header")[4].style.display = "none";

var arr = document.getElementsByClassName("wi_buttons_wrap");
for(var i=0; i<arr.length; i++)
{
    arr[i].style.display = "none";
}