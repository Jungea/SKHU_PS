package net.skhu.etc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "net.skhu.upload")
public class UploadProperties {
    String localPath;
    String urlPath;
}

