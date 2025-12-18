const fs = require('fs');

console.log(' Generating inline Swagger UI...');

// 1. 기존 파일 삭제
if (fs.existsSync('/docs/index.html')) {
    if (fs.statSync('/docs/index.html').isDirectory()) {
        fs.rmSync('/docs/index.html', { recursive: true });
    } else {
        fs.unlinkSync('/docs/index.html');
    }
}

// 2. OpenAPI JSON 읽기
let openApiJson;
try {
    openApiJson = JSON.parse(
        fs.readFileSync('./src/main/resources/openapi.json', 'utf8')
    );
} catch (e) {
    console.error(' Error: Cannot read openapi.json');
    process.exit(1);
}

// 3. HTML 생성 (JSON 인라인)
const html = `<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${openApiJson.info?.title || 'API Documentation'}</title>
  <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui.css" />
  <style>
    html { box-sizing: border-box; overflow-y: scroll; }
    *, *:before, *:after { box-sizing: inherit; }
    body { margin: 0; padding: 0; }
  </style>
</head>
<body>
  <div id="swagger-ui"></div>
  <script src="https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui-bundle.js" crossorigin></script>
  <script src="https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui-standalone-preset.js" crossorigin></script>
  <script>
    window.onload = function() {
      //  JSON이 HTML에 직접 포함됨
      const spec = ${JSON.stringify(openApiJson, null, 2)};
      
      window.ui = SwaggerUIBundle({
        spec: spec,  // URL 대신 spec 직접 전달
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
          SwaggerUIBundle.presets.apis,
          SwaggerUIStandalonePreset
        ],
        plugins: [
          SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
      });
    };
  </script>
</body>
</html>`;

// 4. 파일 저장
fs.writeFileSync('./docs/index.html', html);
console.log(' File size:', Math.round(fs.statSync('./docs/index.html').size / 1024), 'KB');