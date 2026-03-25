<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Alpine.js for interactivity -->
    <script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .btn-dark-blue {
            @apply bg-sky-900 hover:bg-sky-800 text-white;
        }
        .sidebar-link:hover, .sidebar-link.active {
            @apply bg-gray-100 text-sky-900;
        }
        .sidebar-link svg {
            @apply transition-colors duration-200;
        }
        .sidebar-link:hover svg, .sidebar-link.active svg {
            @apply text-sky-900;
        }
        .pagination-link {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-medium;
        }
        .pagination-current {
            font-weight: 900;
            color: cornflowerblue;
        }
        .pagination-first-last {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-bold;
        }
        .sort-icon {
            @apply inline-block text-xs text-gray-400 ml-1 transition-transform duration-200;
        }
        .sort-asc .sort-icon {
            @apply text-sky-700 transform rotate-0;
        }
        .sort-desc .sort-icon {
            @apply text-sky-700 transform rotate-180;
        }
        .py-0.5 {
            padding-top: 0.5rem;
            padding-bottom: 0.5rem;
        }
        .p-09rem {
            padding-top: 0.9rem;
            padding-bottom: 0.9rem;
        }
        .logo-padding {
            padding-top: 0.9em;
            padding-bottom: 0.9em;
        }
        .all-positions {
            padding-top: 0.6em;
            padding-bottom: 0.6em;
            padding-left: 1.5em;
            padding-right: 1.5em;
        }

    </style>
</head>

<body class="bg-gray-50 min-h-screen flex">
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <header class="bg-sky-900 text-white shadow-sm border-b border-gray-100 all-positions">
            <div class="flex justify-between items-center">
                <h2 class="text-2xl font-semibold text-white">Должности</h2>
                <a href="/positions/new"
                   class="btn-dark-blue px-5 py-2 font-medium shadow transition flex items-center gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 border-l-2 border-transparent hover:border-sky-900">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    Добавить
                </a>
            </div>
        </header>
        <!-- Positions List -->
        <main class="flex-1 p-6 overflow-y-auto">
            <#if positions?has_content>
                    <table class="w-full text-sm text-left text-gray-700">
                        <tbody>
                            <!-- Positions -->
                            <#list positions as position>
                            <tr class="border-b border-gray-100 hover:bg-gray-50 transition">
                                <td class="px-4 py-2 font-medium">${position.name}</td>
                                <td class="px-4 py-2 text-center">
                                  <!-- Edit Action -->
                                  <a href="/positions/edit/${position.id}"
                                    class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                    title="Изменить"><svg class="action-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 30 30" stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                                     </svg></a>
                                  <!-- Delete Action -->
                                  <a href="/positions/delete/${position.id}"
                                    class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                    title="Удалить"><svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 30 30" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                    </svg></a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
            </#if>
        </main>
    </div>
</body>
</html>