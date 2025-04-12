export function addPlugin(file: string, extend: any) {
    let plugin = document.createElement("script");
    plugin.setAttribute(
      "src",
      file
    );
    plugin.async = true;
    document.head.appendChild(plugin);

    let plug = document.createElement("script");
    plug.type = "text/javascript";
    plug.text = `${extend}`;
    document.head.appendChild(plug);
}