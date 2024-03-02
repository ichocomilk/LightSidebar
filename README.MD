# LightSidebar
A library for create a sidebar

### How create a sidebar
This is a easy example:
```
Sidebar sidebar = new LightSidebarLib().createSidebar();
Object[] lines = sidebar.createLines( new String[] { "Line 1", "Line 2", "Line 3"} );
sidebar.setLines(lines);
sidebar.setTitle("Example");

// Send sidebar data. The method "sendLines" support a Collection of players
sidebar.sendLines(player);
sidebar.sendTitle(player);
```

## How to compile:
> [!NOTE]
> We need java 17 to compile
1) Clone the repository
4) Execute in your terminal "mvn install"