package cn.ChengZhiYa.Bot.Class;

import lombok.Data;

@Data
public final class Plugin {
    int Id;
    String PluginName;
    String Version;

    public Plugin(int Id, String PluginName, String Version) {
        this.Id = Id;
        this.PluginName = PluginName;
        this.Version = Version;
    }
}
