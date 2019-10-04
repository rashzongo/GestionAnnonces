function previewThumbnailImage(input) {
    if (input.files) {
        $('#illustrationsPreview').empty()
        var readers = [];
        for(var i=0; i<input.files.length; i++){
            readers[i] = new FileReader();
            readers[i].onload = function(e) {
                var img = document.createElement("img");
                img.setAttribute("src", e.target.result);
                img.setAttribute("width", "100px");
                $('#illustrationsPreview').append(img)
            }
            readers[i].readAsDataURL(input.files[i]);
        }
    }
}

$("#thumbnailFile").change(function() {
    previewThumbnailImage(this);
});
