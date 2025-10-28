curl --location 'http://localhost:8080/upload/s3' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YXRtTXNVc2VyOm0zYnJWSHlQ' \
--form 'file=@"/C:/Users/ext_daothanh/Downloads/DOKY description.rtf"'



curl --location 'http://localhost:8080/upload/local' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YXRtTXNVc2VyOm0zYnJWSHlQ' \
--form 'file=@"/C:/Users/ext_daothanh/Downloads/Biznis detail.docx"'